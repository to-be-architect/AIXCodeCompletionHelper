# AIXCodeCompletionHelper

![Build](https://github.com/tuchg/ChinesePinyin-CodeCompletionHelper/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/14838.svg)](https://plugins.jetbrains.com/plugin/14838)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/14838.svg)](https://plugins.jetbrains.com/plugin/14838)
![GitHub Repo stars](https://img.shields.io/github/stars/tuchg/ChinesePinyin-CodeCompletionHelper?color=green&logo=github&style=flat)
[![star](https://gitee.com/tuchg/ChinesePinyin-CodeCompletionHelper/badge/star.svg?theme=white)](https://gitee.com/tuchg/ChinesePinyin-CodeCompletionHelper/stargazers)

<!-- Plugin description -->

<a href="https://github.com/tuchg/ChinesePinyin-CodeCompletionHelper">Github</a>|
<a href="https://gitee.com/tuchg/ChinesePinyin-CodeCompletionHelper">Gitee</a>
| <a href="https://github.com/tuchg/ChinesePinyin-CodeCompletionHelper/issues">Issues</a>

<!-- E -->


## FAQ

### 设置特定平台进行本地测试

以 PhpStorm 为例，在 build.gradle.kts 中添加：
```
tasks.runIde {
    ideDir.set(File("/Applications/PhpStorm.app/Contents"))
}
```

### runide 报错

[java.lang.NoClassDefFoundError: com/intellij/openapi/util/SystemInfoRt](https://intellij-support.jetbrains.com/hc/en-us/community/posts/6826927374226-Unable-to-build-plugin-for-2022-2-after-updating-settings-gradle)。

升级 gradle plugin 版本：`id("org.jetbrains.intellij") version "1.7.0"`

## Thanks

Development powered by [JetBrains](https://www.jetbrains.com/?from=ChinesePinyinCodeCompletionHelper).

[![JetBrains](jetbrains.svg)](https://www.jetbrains.com/?from=ChinesePinyinCodeCompletionHelper)

Whichever technologies you use, there's a JetBrains tool to match.

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
