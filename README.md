# VoiceSearch
## Powered by HUAWEI Search Kit and ML Kit
Takes your voice input, Performs a web search with your given query and provides the search results by voice output.

# How to run
1. Clone the project and change the applicationId under the app-level build.gradle
2. Create a new project on [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html#/) and add the new applicationId as package name
3. From AppGallery Connect, go to **Manage APIs** and then enable Search kit and ML kit
4. Create a keystore and replace the signature information under signingConfigs at the app-level build.gradle
5. From AppGallery Connect, download the agconnect-services.json file and paste it under your app folder
6. run the project or build an apk

For detailed instructions, please follow [this guide](https://forums.developer.huawei.com/forumPortal/en/topic/0202537426455050190)
