# PT-Cracker
---

## 介绍

因为PT协议固有的缺陷，Tracker服务器需要Peer客户端反馈上传和下载的流量。很明显，可以通过作弊来提高上传量，防止因分享率过低而被PT站点封号。

PT-Cracker的目标就是提高你PT账户的上传量，它利用了从某一特定站点(清影PT)下载的`.torrent`文件，获取一些其中的元信息。由于tracker端会对接收到的客户端反馈做一些检测来防止欺骗，但是这种测试比较简单，在PT－Cracker中被完美绕过。本工具每`interval`秒发送一次客户端反馈，并且为了防止因为流量过大（比如上传流量高于1GB/s)而被tracker发现，限定了上传速度的范围（配置文件中），并且一次实际反馈中的上传速度是一个随机值，这样更符合实际情况（做种）。一次反馈的最终上传流量就会是
`interval * randomRate`。总而言之，PT－Cracker可以完美躲避Tracker服务器作弊检测而提高PT的分享率。

## 如何使用

1. 从Github Release上下载 [ptcracker.jar](https://github.com/hoverwinter/ptcracker/releases/download/v0.1.0/ptcracker.jar)

2. 从PT站点下载torrent文件，保存在任一目录（或者当前目录下torrents子目录)

3. 修改配置文件[ptcracker.config](https://github.com/hoverwinter/ptcracker/releases/download/v0.1.0/ptcracker.config)中`torrentsDir`为该目录路径（或者使用默认配置）

4. 使用以下命令执行：

    java -cp  <DOWNLOADED_JAR.jar>  me.carpela.network.pt.cracker.main.Main [config_file]

注意，运行环境需要JRE-1.7或者更高版本的JRE。

命令行参数`config_file`是可选的，如果未给定，PT－Cracker将从当前目录查找`ptcracker.config`。默认的`torrentsDir`目录是当前目录下的`torrents`子目录。

程序的日志将会打印在标准输出和当前目录的`ptcracker.log`文件中。

## 配置

配置文件参考 [ptcracker.config](https://github.com/hoverwinter/ptcracker/blob/master/ptcracker.config)，下面是每一项解释：

- maxRate: 最大上传速度，默认 10240 KB/s
- minRate: 最小上传速度，默认 1024 KB/s
- interval: 工作频率，默认30秒
- total: 一次工作总的上传流量，默认 4096MB
- torrentsDir: 保存`.torrent`文件的目录，默认当前目录下的`torrents`子目录

注意，`torrentsDir`支持子目录。

PT-Cracker在`maxRate`和`minRate` (KB/s)之间随机选择一个速度，每隔`interval`秒发送一次包含按照这个速度这段时间的上传量。为了做特定资源的欺骗，所以需要从PT网站上下载`.torrent`文件，将下载的文件放到｀torrentsDir`目录下。当该目录下有超过10个文件时随机选择10个文件使用。

## FAQ

1. 必须使用我的帐户下载的Torrent文件吗？

是的，因为Torrent文件包含下载它的帐户的唯一身份信息，这样PT站点才知道你的流量信息。

## 问题

请在 [Github issues](https://github.com/hoverwinter/ptcracker/issues) 中提出，如果是使用过程中问题，最好附带相关部分的日志。

## 许可

[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0)