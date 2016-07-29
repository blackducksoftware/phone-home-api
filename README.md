#Phone Home API
##Author: Nicholas Rowles
##Owner: BlackDuck Software, Inc.

The phone-home-api is used to send information to the BlackDuck Software internal Integrations server. The 
```java
PhoneHomeClient
```
class is used to send a 
```java
PhoneHomeInfo
```
object to the server.

From an integrations standpoint. The 
```java
PhoneHomeClient.callHomeIntegrations(String regId, String blackDuckName, String blackDuckVersion, String thirdPartyName, String thirdPartyVersion, String pluginVersion)
```
method should be used to send the relevant information to the server.