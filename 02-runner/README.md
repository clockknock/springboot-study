启动时添加以下参数:
```bash
--greet=["hello","and","world"] --date=20190111 --myNonOptionArg notOptionArg
```

`MyRunner.java`中的`run()`会运行并打印以下内容:
```bash
nonOptionArg:notOptionArg
optionName:date
	 value:20190111
optionName:myNonOptionArg
optionName:greet
	 value:[hello,and,world]
```

添加`Order()`注解后,Runner的运行就会有顺序,数字越小的优先级越高:
```bash
order is 100:pri.zhong.springboot.runner.bean.FirstRunner
nonOptionArg:notOptionArg
optionName:date
	 value:20190111
optionName:myNonOptionArg
optionName:greet
	 value:[hello,and,world]
order is 180:pri.zhong.springboot.runner.bean.SecondRunner
```