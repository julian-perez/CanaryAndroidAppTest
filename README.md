# SETUP

## Installing Java

Download and install [Java JDK] (<http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>)

If it fails, check if you have the permission to write the file into the folder.

Change it by following these steps

- Reboot
- log in the Mac in Recovery mode by pressing CTRL + R right before the chime
- Go to utilities -> Terminal
- Type in

```
csrutil disable; reboot
```

- You should be good to install it now.

Download and install the [Java JRE] (<http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html>)

## Generating SSH Key for GitHub

[Generating and add the SSH-key for your GitHub account.] (<https://help.github.com/articles/generating-an-ssh-key/>)

Install [Homebrew] using the following command in the Terminal

```
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

## Installing Android-SDK & Android-ND

Install android-SDK using the following command

```
brew install android-sdk
```

Install android-NDK using the following command

```
brew install android-ndk
```

NOTE: Android app won't build with the latest NDK. Use above command to install the latest NDK on your machine, then download `r11c` version (https://developer.android.com/ndk/downloads/older_releases.html#ndk-11c-downloads) and put in your `/usr/local/Cellar/android-ndk` folder. Rename the new folder into `r11c` to match the naming convention. Then, run this command: `brew switch android-ndk r11c`. App should now build.

## Installing Android-Studio

Download and install [android studio] (<https://developer.android.com/studio/index.html>)

After installation using the android SDK make sure to download all the tools and SDK up until version 16

## Setting up the bash_profile

Create a bash_profile using the following command

- Start up Terminal

- Type the code below to go to your home folder

  ```
  cd ~/
  ```

- Type the code below to create your new file.

  ```
  touch .bash_profile
  ```

- Edit .bash_profile with your favorite editor or you can just type the code below to open it in TextEdit.

  ```
  open -e .bash_profile
  ```

- Add the following code to the bash_profile

  ```
  ## Android SDK
  export ANDROID_SDK= //Location of the Android-SDK
  export ANDROID_NDK=/usr/local/Cellar/android-ndk/r7b
  ```

- Type in the code below to reload .bash_profile and update any functions you add.

  ```
  . .bash_profile
  ```

## Cloning & Updating the Canary app

Clone the Canary Android app from the github repository

```
git clone https://github.com/cnry/android.git
```

To get a module from the repository

```
cd "submodule-name"
git submodule init
```

Update the repository

```
git submodule update
```
