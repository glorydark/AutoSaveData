# AutoSaveData
定时/手动保存服务器特定文件夹！

## How to use this plugin？​
  Firstly, move this plugin into the dictionary '/plugins/' 
  Command Usage: /savealldata​
  This command allows you to save all dictionaries which has edited in the profile 'config.yml'
  You can change save interval by editing this profile as well
## config.yml
### AutoSaveIntervals: 60 #分钟为单位！请根据您安排的定时重启任务来修改！
### SaveDir:
#### "players/"文件夹不可以，服务器一直在使用此文件夹，保存此文件夹会导致报错
#### 报错是因为服务器占用某文件夹！
#### 请按此格式编辑！
#### - "plugins/EconomyAPI"
#### - "plugins/DAwards"
#### - "worlds/"
