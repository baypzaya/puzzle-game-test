/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package com.idreamsky.ktouchread.yunpay;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.aliyun.android.app.IAliyunPay;
import com.aliyun.android.app.IRemoteServiceCallback;

public final class AliyunPayProxy {

    private Activity            activity;
    private volatile IAliyunPay pay            = null;
    private boolean             binding        = false;
    private Object              sync           = new Object();
    private AtomicBoolean       run            = new AtomicBoolean(true);

    private AtomicBoolean       serviceCanBind = new AtomicBoolean(false);

    private ServiceConnection   conn           = new ServiceConnection() {
                                                   public void onServiceConnected(ComponentName className,
                                                                                  IBinder service) {
                                                       synchronized (sync) {
                                                           assert pay == null : "pay==null";
                                                           pay = IAliyunPay.Stub
                                                               .asInterface(service);
                                                           sync.notifyAll();
                                                       }
                                                   }

                                                   public void onServiceDisconnected(ComponentName className) {

                                                       assert pay != null : "pay!=null";
                                                       synchronized (sync) {
                                                           pay = null;
                                                       }

                                                       if (binding) {
                                                           bindAliyunPayService();
                                                       }
                                                   }
                                               };

    public AliyunPayProxy(Activity activity) {
        this.activity = activity;
    }

    public boolean isBinding() {
        return binding;
    }

    public boolean bind() {
        if (!binding) {
            binding = true;
            return bindAliyunPayService();
        }
        return true;
    }

    public void unbind() {
        if (binding) {
            binding = false;
            synchronized (sync) {
                pay = null;
            }
            unbindAliyunPayService();
        }
    }

    public void stop() {
        run.set(false);
        synchronized (sync) {
            sync.notifyAll();
        }
    }

    public void start() {
        run.set(true);
    }

    public String pay(final String orderInfo) {
        if (canPay()) {
            try {
                IAliyunPay tPay;
                synchronized (sync) {
                    while (pay == null && canPay()) {
                        sync.wait();
                        if (!canPay()) {
                            return null;
                        }
                    }
                    tPay = pay;
                }

                if (tPay != null) {
                    tPay.registerCallback(callback);
                    final String result = tPay.Pay(orderInfo);
                    tPay.unregisterCallback(callback);
                    return result;
                }

            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public boolean canPay() {
        return run.get() && serviceCanBind.get();
    }

    private boolean bindAliyunPayService() {
        final boolean success = activity.bindService(new Intent(IAliyunPay.class.getName()), conn,
            Context.BIND_AUTO_CREATE);
        serviceCanBind.set(success);

        if (!success) {
            synchronized (sync) {
                sync.notifyAll();
            }
        }
        return success;
    }

    private void unbindAliyunPayService() {
        activity.unbindService(conn);
    }

    private IRemoteServiceCallback callback = new IRemoteServiceCallback.Stub() {
                                                public void startActivity(String packageName,
                                                                          String className,
                                                                          int iCallingPid,
                                                                          Bundle bundle)
                                                                                        throws RemoteException {
                                                    Intent intent = new Intent(Intent.ACTION_MAIN,
                                                        null);

                                                    intent.putExtras(bundle);

                                                    intent.setClassName(packageName, className);
                                                    activity.startActivity(intent);
                                                }
                                            };
}