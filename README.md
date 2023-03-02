# railway-12306-transfer（开发ing）
## 项目介绍
该项目可以生成最全的12306中转方案！！！通过12306查询车票页面生成中转方案，解决了12306官方生成中转方案不全的问题，并且此项目无需登录12306。 
## 项目背景
春节回杭州的时候，遇到一个哥们，从兰州到杭州，由于没有抢到票，一直站了一天多。我问他为什么不中转一下，他说中转站太多了，不知道在哪里中转，而且12306的中转方案都是高铁动车什么的，太贵了。再加上自己回家也需要中转，所以创建了这个项目，只是为了给远在他乡的打工人能找到一个性价比（时间、金钱）最高的回家方案。
## 项目运行步骤
本项目以ChromeDriver为例展开项目运行步骤，要是想用别的浏览器也是可以的，只需要将代码中的ChromeDriver换成对应浏览器的Driver即可，当然，驱动也要换成对应的驱动。
1. 首先要先拥有一个chrome浏览器，然后打开浏览器的设置，查看浏览器的版本；
2. 打开https://registry.npmmirror.com/binary.html?path=chromedriver/ 下载对应浏览器版本以及对应系统版本的chromedriver；
3. Mac OS安装方式：
   1. 下载并解压，将解压出来的chromedriver存放到 /usr/local/bin 这个目录下；
   2. 执行如下命令
		1. 删除chromedriver的隔离性
`xattr -d com.apple.quarantine chromedriver`
		2. 对chromedriver添加许可
`spctl --add --label 'Approved' chromedriver`
	3. 打开终端，输入chromedriver并回车，可成功启动一个浏览器窗口即为驱动安装成功。
4. Windows安装方式：
	1. 下载并解压，将解压出来的chromedriver.exe存放到谷歌浏览器安装目录下，比如：C:\Program Files (x86)\Google\Chrome\Application；
	2. 配置环境变量:此电脑→右击属性→高级系统设置→环境变量→用户变量→Path→编辑→新建，也就是刚刚放chromedriver.exe的路径，比如：C\:\Program Files (x86)\Google\Chrome\Application；
	3. 打开cmd，输入chromedriver并回车，可成功启动一个浏览器窗口即为驱动安装成功。
## 开发日志
1. 项目创建（2023-02-08）；
2. 运行起来项目（2023-02-10）；
3. 引入Knife4j、Selenium等依赖并配置（2023-02-12）；
4. 完成中转车票的信息的生成（2023-02-20）；
5. 基本中转方案的生成（2023-02-24）;
6. 最终中转方案生成（2023-02-27）；
7. 安装使用文档补充（2023-03-02）。
## 待办列表
1. 引入日志；
2. 能够选择车站。