proguard-android.txt的路径和作用
sdk\tools\proguard\proguard-android.txt
release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
        }
