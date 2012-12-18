package com.ktouch.pcs;

interface IPCSManager {
    boolean upload(String path);     
    boolean download(String path);  
}