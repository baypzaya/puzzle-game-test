LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := game_shared

LOCAL_MODULE_FILENAME := libgame

LOCAL_SRC_FILES := hellocpp/main.cpp \
                   Classes/AppDelegate.cpp \
                   Classes/GameData.cpp \
                   Classes/HelloWorldScene.cpp \
		   		   Classes/GameOverScene.cpp \
		   		   Classes/Enimy.cpp \
		   		   Classes/Bullet.cpp \
		   		   Classes/DefenceTower.cpp
                   
LOCAL_C_INCLUDES := $(LOCAL_PATH)/Classes                   

LOCAL_WHOLE_STATIC_LIBRARIES := cocos2dx_static cocosdenshion_static cocos_extension_static
            
include $(BUILD_SHARED_LIBRARY)

$(call import-module,CocosDenshion/android) \
$(call import-module,cocos2dx) \
$(call import-module,extensions)
