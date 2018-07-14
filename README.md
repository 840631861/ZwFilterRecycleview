### ZwFilterRecycleview
[ZwFilterRecycleview](https://github.com/840631861/ZwFilterRecycleview)是一款开源的Android RecycleView控件。
集成了多种条件筛选（也可自定义添加）、排序、上拉加载下拉刷新等，样式颜色、图片、字体都可自定义

![image](https://github.com/840631861/ZwFilterRecycleview/blob/master/app/src/main/resource/%E7%A4%BA%E4%BE%8B1.gif)

### 使用
**Step1：添加依赖**

Gradle
```xml
allprojects {
  repositories {
    ...
    maven { url 'https://www.jitpack.io' }
  }
}
```
```xml
dependencies {
        implementation 'com.github.840631861:ZwFilterRecycleview:v0.1.6'
}
```
Maven
```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://www.jitpack.io</url>
  </repository>
</repositories>
```
```xml
<dependency>
    <groupId>com.github.840631861</groupId>
    <artifactId>ZwFilterRecycleview</artifactId>
    <version>v0.1.6</version>
</dependency>
```

**Step2：添加布局**

更过自定义属性[参见](#自定义属性说明)<BR />
```xml
<com.project.view.ZwFilterRefreshView
    android:id="@+id/view"                                  
    app:barTxtSize="15dp"                                
    android:layout_width="match_parent"
    android:layout_height="match_parent">
</com.project.view.ZwFilterRefreshView>
```

**Step3：在Activity或Fragment中配置数据源和样式**

***添加筛选侧拉栏中单选、多选数据及监听事件***
```java
 //设置侧拉栏数据及监听
view.getFilterManager()
    //.addCheckData(data)//添加单个侧拉栏中数据（选择按钮）
    .addCheckDatas(checkDatas)//添加多个侧拉栏中数据(选择按钮)
    .setOnFilterItemChangeListener(new IListView.OnFilterItemChangeListener() {
        @Override
        public void onFilterItemChange(FilterData data) {
            //点击或改变筛选中的item数值后的回调
        }
    })
    .setOnFilterConfirmClickListener(new IListView.OnFilterConfirmClickListener() {
        @Override
        public void onFilterConfirmClick(FilterData data) {
            //点击确认按钮的监听
            Toast.makeText(context,"点击了确认",Toast.LENGTH_SHORT).show();
            List<FilterCheckData> list = data.getCheckDatas();
        }
    })
    .setOnFilterResetClickListener(new IListView.OnFilterResetClickListener() {
        @Override
        public void onFilterResetClick(FilterData data) {
            //点击重置按钮的监听
            Toast.makeText(context,"点击了重置",Toast.LENGTH_SHORT).show();
            List<FilterCheckData> list = data.getCheckDatas();
        }
    });
```

***自定义修改侧拉栏中样式，添加时间段条件、搜索框等，还可以添加自定义布局***
```java
View custom = LayoutInflater.from(context).inflate(R.layout.layout_custom,null);
//设置侧拉栏样式
view.getFilterManager()
    //.addTimeSection(getSupportFragmentManager(), Type.YEAR_MONTH)//添加时间段（不带样式使用默认）
    .addTimeSection(getSupportFragmentManager(), Type.YEAR_MONTH,R.color.colorAccent)//添加时间段（带样式）
    .addSearchTxt()//添加搜索框
    .addCustomView(custom)//添加自定义布局
    .setOnAddCustemViewCallback(new IListView.OnAddCustemViewCallback() {
        @Override
        public void onAddCustemView(View parent, View custom) {
            //自定义布局回调
        }
    });
```

***更改顶部栏中排序、筛选等按钮的样式，及设置下拉菜单的数据***
更多设置[参见](#顶部栏样式设置)<br /> 
```java
//设置顶部栏样式
view.getViewBarManager()
    //.setBarTxtSize(15)
    //.setBarImgCom(getResources().getDrawable(R.mipmap.ic_tri_down),getResources().getDrawable(R.mipmap.ic_tri_up))
    .setComSpinnerData(spinnerData)
    .setFilterTxt("筛选")
    .setOnSortClickListener(new IListView.OnSortClickListener() {
        @Override
        public void onSortClick(int status) {
            Toast.makeText(context,"点击了排序："+status,Toast.LENGTH_SHORT).show();
        }
    })
    .setOnComSpinnerSelectedListenner(new IListView.OnComSpinnerSelectedListener() {
        @Override
        public void onComSpinnerSelected(int index) {
            Params params = view.getBarCurData();
            Toast.makeText(context,"点击了："+spinnerData.get(index),Toast.LENGTH_SHORT).show();

        }
    })
    .setComSpinnerSelectedIndex(0);
```
注意：修改完样式后需要调用
```java
view.updateView();
```

***设置recycleview列表数据***

1、view.getRecyclerView()获取recycleview实例，然后自定义数据

2、其中提供了封装的adapter（BaseRecyclerAdapter）供使用，如
```java
public class NomalAdapter extends BaseRecyclerAdapter
{
    private Context context;
    public NomalAdapter(Context context,List<Map<String,String>> data) {
        this.context = context;
        setData(data);
    }

    @Override
    public View getHolderView(ViewGroup parent, int position) {
        View root = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return root;
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewHolder holder, int position) {
        TextView tv = holder.getView(R.id.tv_name);
        Map<String,String> item = (Map<String, String>) _data.get(position);
        tv.setText(item.get("id"));
    }
}
```
```java
List<Map<String,String>> list = getData();
NomalAdapter adapter = new NomalAdapter(context,list);
view.getRecyclerView().setLayoutManager(new LinearLayoutManager(context));
view.getRecyclerView().setAdapter(adapter);
```

### 顶部栏样式设置

```java
//设置顶部栏按钮字体大小
setBarTxtSize

//设置顶部栏文字
setSortTxt
setFilterTxt

//设置顶部栏高度
setBarHeight

//设置顶部栏字体颜色
setBarTxtColor
setBarTxtColorActive

//设置顶部栏综合按钮图标(下拉菜单图标)
setBarImgCom

//设置排序按钮图标
setBarImgSot

//设置顶部栏筛选图标按钮
setBarImgFilter

//设置综合下拉菜单数据
setComSpinnerData

//设置下拉菜单选中
setComSpinnerSelectedIndex

//设置、获取排序状态
getSortStatus
setSortStatus

//隐藏、显示顶部栏
hideBar(Boolean isHide)

//隐藏、显示顶部栏中的综合按钮、排序按钮、筛选按钮
hideBar(Boolean isHideComBtn, Boolean isHideSortBtn, Boolean isHideFilterBtn)
 
```

### 自定义属性说明

```xml
<declare-styleable name="ZwFilterRefreshView">
    <!-- 标题栏字体大小 -->
    <attr name="barTxtSize" format="dimension"></attr>
    <!-- 标题栏高度 -->
    <attr name="barHeight" format="dimension"></attr>
    <!-- 标题栏颜色 -->
    <attr name="barTxtColor" format="color"></attr>
    <!-- 标题栏选中颜色 -->
    <attr name="barTxtColorActive" format="color"></attr>
    <!-- 下拉菜单图片 -->
    <attr name="barImgCom" format="reference"></attr>
    <!-- 下拉菜单下拉时的图片 -->
    <attr name="barImgComActive" format="reference"></attr>
    <!-- 排序按钮图标 -->
    <attr name="barImgSortDefault" format="reference"></attr>
    <attr name="barImgSortAsc" format="reference"></attr>
    <attr name="barImgSortDesc" format="reference"></attr>
    <!-- 筛选按钮图标 -->
    <attr name="barImgFilter" format="reference"></attr>
    <!-- 排序按钮文字 -->
    <attr name="barSortText" format="string"></attr>
    <!-- 筛选按钮文字 -->
    <attr name="barFilterText" format="string"></attr>
</declare-styleable>
```

### 附
minSdkVersion 19  targetSdkVersion 27
