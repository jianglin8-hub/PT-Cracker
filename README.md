# PT-Cracker
---

## Introduction

Since the PT-Protocol have its inner pitfalls, the tracker server relys on the client of peer's feedback of the traffic (eg. upload/download), it's obvious that you can send a packet to cheat the server with fake upload trafffic.

This tool aims to increase your upload traffic of your PT account, using your `.torrent` file downloaded from some PT website. For the reson that the tracker always check the request parameter simply to prevent cheats from peer client. The PT-Cracker send the packet every `interval` seconds, additionally, the upload rate is between `maxRate` and `minRate`. That is, a `randomRate` in the range is chosen, and upload traffic finally comes up to `interval * randomRate`. In conclusion, PT-Cracker is difficult to be inspected by tracker.

## How to use?

1. Download the `jar` file from release page on Github.

2. Download the `torrent` file from PT website.

3. Modify the `torrentsDir` item in config file.

4. Usage:

    java -cp  <DOWNLOADED_JAR.jar>  me.carpela.network.pt.cracker.main.Main [config_file]

Note that `JRE-1.7` or above is required.

Or:
	
	git clone https://github.com/hoverwinter/ptcracker ptcracker

and export to some IDE or use `maven` to fit your need.

CLI argument `config_file` is optional, if not specified, it will find the `ptcracker.config` in your current directory.

Default `torrentsDir` is `torrents` subdirectory in your current directory.

Also, logs will be printed on `STDOUT` and `ptcracker.log` in your current directory.

## Config

The PT-Cracker send the data at a rate between `maxRate` and `minRate` (KB/s) every `interval` seconds. And `total` upload traffic is cheated at each execution. You should put your downloaded `.torrent` file from a PT website at `torrentsDir`. If there is more than 10 files, only 10 are selected throgh a shuffle stage.

- maxRate: maximum upload rate, default 10240 KB/s
- minRate: minimum upload rate, default 1024 KB/s
- interval: working frequency, default 30s
- total: total upload traffic, default 4096MB
- torrentsDir: directory that saves the `.torrent` files, default `./torrents/`

*** Notice: subdirectories are supported in `torrentsDir`. ***

## Addition

You're not supposed to rely on PT-Cracker, it's recommended that opening a BT client and share your data on your disk.

It's possible to fix this pitfall by confirming upload traffic from other clients in the peers.

## References

- [README中文版](README_CN.md)

## FAQ

1. Do I have to use the torrrent file downloaded by my account?

Yes, because the file contains the unique identifier of your account, from which PT website can obtain the traffic information.

## Any Questions?

Please describe them in Github issues. If something goes wrong when execute the program, please attach the log concerned.

## License

Licensed under the Apache License, Version 2.0 (the "License");

you may not use this file except in compliance with the License.

You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software

distributed under the License is distributed on an "AS IS" BASIS,

WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.

See the License for the specific language governing permissions and

limitations under the License.


Copyright @ Hover Winter <hoverwinter@gmail.com> 2016
