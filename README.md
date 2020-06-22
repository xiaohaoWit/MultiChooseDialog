# MultiChooseDialog
多级选择器
## How to use
gradle
Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.liulang-alin:MultiChooseDialog:-SNAPSHOT'
	}
	
或

Step 1. Add the JitPack repository to your build file
maven
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
Step 2. Add the dependency

	<dependency>
	    <groupId>com.github.liulang-alin</groupId>
	    <artifactId>MultiChooseDialog</artifactId>
	    <version>-SNAPSHOT</version>
	</dependency>

### 使用

    DggMultistageDialog multistageDialog =new DggMultistageDialog.Builder(this)
        .setTitle("选择所在地区")
        .setCurrentColor(0xff525BDF)
        .build();
    multistageDialog.setListener(new DggMultistageDialog.OnChooseItemListener<ProvinceData>() {
        @Override
        public void onChoose(ProvinceData data,List<ProvinceData> dataList) {
	    ///每次选择后的回调，可根据需要关闭对话框或继续添加下一级的数据，这个过程中如果配置了loadView会显示
            if (data!=null&&data.getChildren()!=null&&data.getChildren().size()>0){
                multistageDialog.addData(data.getChildren());
            }else {
                multistageDialog.dismiss();
                String value="";
                for (ProvinceData provinceData:dataList){
                    value=value+provinceData.getName();
                }
                ToastUtils.showShort(value);
            }
        }
    });
    multistageDialog.show();  
    multistageDialog.addData(jsonBean); ///添加数据

	可配置属性
    //当前级选中字体颜色
    private int currentColor=0xff10BBB8;
    //前面级选中的字体颜色
    private int normalColor=0xff333333;
    //关闭按钮图标资源 默认不显示
    private int closeBtnRes=-1;
    //标题颜色
    private int titleColor=0xff333333;
    //标题字体大小
    private int titleSize=18;
    //选中了的字体大小
    private int selectSize=14;
    private Context context;
    //标题文本
    private String title;
      //数据加载框
    private View loadingView;

	其中的数据需要实现接口MultistageData
    public interface MultistageData {
        //返回显示的字段
        String getValue();
    
        //选中颜色
        @ColorInt
        int getSelectedColor();
    
    	 //正常颜色
         @ColorInt
        int getNormalColor();
    }
