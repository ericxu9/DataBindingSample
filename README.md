简书地址：https://www.jianshu.com/p/c9a0b0c53524

Android DataBinding Library使用指北

DataBinding（数据绑定库）是Google提供的一个支持库，能够帮助我们减少应用程序和布局之间的一些冗余代码，比如我们以前常写的`findViewById`，不仅如此；我们所说的MVVM模式也是基于它来实现的，下面让我们来了解它的更多好处吧。

#### 一. 构建环境

> 注意：DataBinding只能往下兼容到**Android 2.1**（API 7+），Android Studio的Gradle插件版本必须高于**1.5.0-alpha1**，并且Android Studio v1.3+才能使用.

在module的build.gradle中开启dataBinding

```gradle
android {
    ....
    dataBinding {
        enabled = true
    }
}
```

#### 二. DataBinding简单体验

我们先来定义一个java bean对象，很普通的一个User类

```java
public class User
{
    private String name;
    private int    age;

    public User(String name, int age)
    {
        this.name = name;
        this.age = age;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

接来下编写我们的布局，DataBinding布局文件和普通布局文件有些不同，它是以`<layout>`为根布局，后面跟着`<data>`元素紧跟着是`view`的根布局。

```xml
<?xml version="1.0" encoding="utf-8"?>

<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>
        <variable
            name="user"
            type="com.xuyj.databinding.sample.User"/>
    </data>

    <LinearLayout
        xmlns:bind="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context="com.xuyj.databinding.sample.BasicUsageActivity">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{user.toString()}"
            android:textColor="@color/colorBlack"
            android:textSize="16sp"/>

    </LinearLayout>

</layout>
```

最后我们编写Activity的代码。

```java
public class BasicUsageActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityBasicUsageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_basic_usage);
        User user = new User("许渺", 18);
        binding.setUser(user);
    }
}
```
Activity的代码很简单，关于布局里面的**TextView**我们并没有使用**findViewById**来**setText**，有木有瞬间感觉代码简洁了不少？

![.](https://ws4.sinaimg.cn/large/006tNc79gy1forjhkivfwj30m40660tb.jpg)

#### 三. 布局分析

上面的代码我们只是粗略的了解了DataBinding，接下来让我们从布局文件入手，正式开始学习DataBinding。

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="user"
            type="com.xuyj.databinding.sample.User"/>
    </data>
</layout>
```

首先和普通布局不同的是，根节点变成了`layout`，它分为两部分；一部分是data元素，另一部分是我们的视图元素；data下我们定义了variable（代表变量），name代表变量名，type是数据的类型（全路径）。
变量定义好了，接下来就是使用了。

```xml
<TextView
	android:layout_width="wrap_content"
	android:layout_height="wrap_content"
	android:text="@{user.toString()}"
	android:textColor="@color/colorBlack"
	android:textSize="16sp"/>
```

使用”@{}“语法来设置值，这里我们设置了user对象toString返回的数据。

#### 五. 事件处理

Data Binding允许我们编写表达式来处理View分发的事件（如`onClick`），事件属性名称由监听器方法进行管理，例如`View.OnLongClickListener`有一个方法`onLongClick`，所以这个属性的是`android:onLongClick`,处理事件有两种方法：

* 方法引用

定义监听方法,这个和直接在布局文件中写`onClick=“method”`类似，这里就不多赘述了。

```java
public class Presenter
{
	public void onClick(View view){...}
}
```

在布局中添加点击事件表达式

```xml
<?xml version="1.0" encoding="utf-8"?>

<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="presenter"
            type="com.xuyj.databinding.sample.BasicUsageActivity.Presenter"/>
    </data>
        <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:onClick="@{presenter::onClick}"
            android:text="点我^_^(Method)"/>
    </LinearLayout>

</layout>
```

* 监听器绑定

它与方法引用类似，但是它允许运行任意数据绑定表达式，并且表达式采用的是lambda表达式；需要注意的是它只适用于Gradle 2.0+版本的Android Gradle插件上（现在应该不会有人的AS低于它吧...），所以相对来说还是比方法引用更加灵活。

还有，布局中表达式的参数必须要和事件监听器的参数匹配，比如 
`onCheckedChanged(CompoundButton buttonView, boolean isChecked);`

```java
public class Presenter
{
	public void completeChanged(boolean isChecked){...}
}
```

```xml
<?xml version="1.0" encoding="utf-8"?>

<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="presenter"
            type="com.xuyj.databinding.sample.BasicUsageActivity.Presenter"/>
    </data>
        <CheckBox
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:onCheckedChanged="@{(view,isChecked) -> presenter.completeChanged(isChecked)}"
            android:text="点我$_$(Listener)"/>
    </LinearLayout>

</layout>
```

#### 布局细节

##### Import

可以帮助我们导入布局文件中的类，和java中的import是一样的。

```xml
<data>
    <import type="com.xuyj.databinding.sample.User"/>
    <import type="android.view.View"/>
    <variable name="user" type="User"/>
</data>
```

##### 使用表达式

```xml
<TextView
   android:text="@{user.lastName}"
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"
   android:visibility="@{user.age > 20 ? View.VISIBLE : View.GONE}"/>
```

当不同包下类名相同时，我们可以定义别名，如下

```xml
<import type="android.view.View"/>
<import type="com.example.real.estate.View"
        alias="Vista"/>
```

**注意：java.lang.\*下的类是自动导包的**

##### 自定义Binding类名

默认情况下，系统会根据布局文件的名称生成一个Binding类，以大写字母开头，以Binding后缀结尾，给类放在databinding包下，例如，布局文件`activity_basic_usage`，会生成`ActivityBasicUsageBinding`如果我们的包名是`com.xuyj.databinding.sample`,那么它会放在`com.xuyj.databinding.sample.databinding`下。

我们可以调整data元素的class属性(修改类名)，例如

```xml
<data class="CustomBinding">
    ...
</data>
```
如果要类在当前包下，只要在前面加”.“就可以了

```xml
<data class=".CustomBinding">
    ...
</data>
```

自定义包名
```xml
<data class="com.example.CustomBinding">
    ...
</data>
```

![。](https://ws4.sinaimg.cn/large/006tNc79gy1formc7l31dj30rg0mqabo.jpg)

##### Includes

变量可以从布局中传递到include包含的布局中

```xml
<?xml version="1.0" encoding="utf-8"?>

<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="user"
            type="com.xuyj.databinding.sample.User"/>
    </data>

    <LinearLayout
        xmlns:bind="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        tools:context="com.xuyj.databinding.sample.BasicUsageActivity">

        <include
            layout="@layout/name"
            bind:name="@{user.name}"/>

    </LinearLayout>

</layout>
```

在这里，include的布局文件也要有对应的变量，name.xml如下：

```xml
<?xml version="1.0" encoding="utf-8"?>

<layout xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable
            name="name"
            type="String"/>
    </data>

    <LinearLayout
        android:layout_width="wrap_content"
        android:layout_height="wrap_content">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="@{name}"/>
    </LinearLayout>

</layout>
```

**Data Binding不支持`include`直接作为`merge`直接的子元素**，例如

```xml
<merge>
	<include
		layout="@layout/name"
		bind:name="@{user.name}"/>
</merge>
```

##### 表达式语言

表达式语言看起来和java是一样的，如下：

* 数学 + - / * %
* 字符串拼接 +
* 逻辑操作符 && ||
* 二进制 & | ^
* 一元符 + - ! ~
* 位移 >> >>> <<
* 比较符 == > < >= <=
* instanceof
* Grouping ()
* 字符 - character, String, numeric, null
* Cast类型转换
* 方法调用
* 属性访问
* 数组访问 []
* 三元操作符 ?:

##### 不支持的表达式

* this
* super
* new
* Explicit generic invocation（比如泛型类）

##### 空合并运算符

空合并运算符（?）,如果左边为空，则选择右边，反之亦然。

```xml
android ：text = “@ {user.displayName ?? user.lastName}”
```
其实上面的就等同于

```xml
android ：text = “@ {user.displayName！= null？user.displayName：user.lastName}”
```

##### 避免NullPointerException

生成的数据绑定代码会自动检查空值并避免空指针异常。例如，在表达式中`@{user.name}`,如果user为null，将会为它分配默认值（null），如果是int类型，默认值为0

##### 集合

数组，List，Map，SparseArray,都可以通过`[]`来进行访问。

```xml
<data>
    <import type="android.util.SparseArray"/>
    <import type="java.util.Map"/>
    <import type="java.util.List"/>
    <variable name="list" type="List&lt;String&gt;"/>
    <variable name="sparse" type="SparseArray&lt;String&gt;"/>
    <variable name="map" type="Map&lt;String, String&gt;"/>
    <variable name="index" type="int"/>
    <variable name="key" type="String"/>
</data>
…
android:text="@{list[index]}"
…
android:text="@{sparse[index]}"
…
android:text="@{map[key]}"

```

#### 六. 数据对象

任何普通的java bean都可以用于数据绑定，但是修改java bean不会导致UI更新，为了解决这个问题，Data Binding提供了三种不同的数据更改通知机制；`可观察对象、可观察字段和可观察集合`。

##### Observable Objects

定义类继承`BaseObservable`，在`getter`方法上加上`@Bindable`，在`setter`方法中调用`notifyPropertyChanged`方法，这里`BR`是记录布局文件中定义的变量，和R文件有点类似。

```java
public class User extends BaseObservable
{
    private String name;
    private int    age;

    public User(String name, int age)
    {
        this.name = name;
        this.age = age;
    }

    @Bindable
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
//        notifyPropertyChanged(BR.age);
    }

    @Override
    public String toString()
    {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

##### ObservableFields

如果你嫌第一种方式麻烦，那你可以考虑换一种口味；Google工程师封装了以下这些类；ObservableField，ObservableBoolean,ObservableByte,ObservableChar,ObservableShort,ObservableInt,ObservableLong,ObservableFloat,ObservableDouble,ObservableParcelable，用法也很简单，如下

```java
public class User {
   public final ObservableField<String> name =
       new ObservableField<>();
   public final ObservableInt age = new ObservableInt();
}
```

接下来该怎么访问呢？也很简单，通过getter，setter来访问，so easy！

```java
user.name.set("许渺");
int age = user.age.get();
```

##### Observable Collections

既然有了变量，集合当然也不能少啊；Android提供了`ObservableArrayMap`和`ObservableArrayList`，用法和java是一样的，我们以`ObservableArrayMap`来举例。

```java
ObservableArrayMap<String, Object> user = new ObservableArrayMap<>();
user.put("name", "许渺");
user.put("age", 18);
```

布局文件中也是一样的套路，先定义变量绑定数据，如下：

```xml
<data>
    <import type="android.databinding.ObservableMap"/>
    <variable name="user" type="ObservableMap&lt;String, Object&gt;"/>
</data>
…
<TextView
   android:text='@{user["name"]}'
   android:layout_width="wrap_content"
   android:layout_height="wrap_content"/>
```

#### 七. 带有id的控件如何访问呢？

这个和`kotlin`的插件很像，非常方便；这个时候就可以丢掉你的`Butterknife`了！

```xml
<TextView
	android:id="@+id/user"
	android:layout_width="wrap_content"
	android:layout_height="wrap_content"
	android:text="@{user.toString()}"
	android:textColor="@color/colorBlack"
	android:textSize="16sp"/>
```

那么在Activity中如果访问呢？我已经猜出来了。

```java
ActivityBasicUsageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_basic_usage);
binding.user.setText("zz");
```

这里要注意的是布局中的命名，如果你使用了下划线“_”的话，那么下划线后面的字母会自动变成大写，这是它的规则！

#### 八. ViewStubs

ViewStub比较特殊，我们单独抽出来说一下，它在调用inflate之前是不可见的，DataBinding使用了`ViewStubProxy`这个类来实现，我们需要设置监听，当布局填充的时候将会调用`onInflate`方法

![.](https://ws3.sinaimg.cn/large/006tNc79gy1fotmcikd4rj31320g6jub.jpg)

这边布局代码就不贴了，我们直接来看一下activity的实现，具体代码可以看下面的源码

```java
public void load(View view)
{
	mBinding.viewStub.setOnInflateListener(new ViewStub.OnInflateListener()
	{
		@Override
		public void onInflate(ViewStub stub, View inflated)
		{
			mBinding.setName("啦啦啦");
		}
	});
	mBinding.viewStub.getViewStub().inflate();
}

```

#### 九. 创建ViewBinding

之前，我们在Activity中都是通过`DataBindingUtil.setContentView(activity,layoutId)`来加载布局的，那么问题来了；如果是Fragment或者是RecyclerView中该怎么办呢？不用担心，DataBinding提供了其它的方法来实现（下面两个是长常用的）。

```java
MyLayoutBinding binding = MyLayoutBinding.inflate(layoutInflater);
MyLayoutBinding binding = MyLayoutBinding.inflate(layoutInflater, viewGroup, false);
```

##### Fragment实践

```java
public class TestFragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        FragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test, container, false);
        binding.setUser(new User("许渺z", 20));
        return binding.getRoot();
    }
}
```

##### RecyclerView实践

```java
public class ListActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ListAdapter(this));
    }


    private class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>
    {

        private       List<User>     mListData;
        private final LayoutInflater mInflater;

        public ListAdapter(Context context)
        {
            mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mListData = new ArrayList<>();
            for (int i = 0; i < 10; i++)
            {
                mListData.add(new User("许渺" + i, i + 10));
            }
        }

        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            ItemListBinding binding = DataBindingUtil.inflate(mInflater, R.layout.item_list, parent, false);
            ViewHolder holder = new ViewHolder(binding.getRoot());
            holder.setBinding(binding);
            return holder;
        }

        @Override
        public void onBindViewHolder(ListAdapter.ViewHolder holder, int position)
        {
            User user = mListData.get(position);
            holder.binding.setVariable(BR.user, user);
            holder.binding.executePendingBindings();
        }

        @Override
        public int getItemCount()
        {
            return mListData.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            ViewDataBinding binding;

            public ViewHolder(View itemView)
            {
                super(itemView);
            }

            public ViewDataBinding getBinding()
            {
                return binding;
            }

            public void setBinding(ViewDataBinding binding)
            {
                this.binding = binding;
            }
        }
    }

}
```

`executePendingBindings()` 这个方法是当变量发生改变时，调用该方法进行强制刷新，嗯！就是这样。

这边运行效果图就不贴了，毕竟界面长得比较丑。

![.](https://ws1.sinaimg.cn/large/006tNc79gy1fotn0pxy61j3046046mx4.jpg)

#### 十. 高级绑定

##### 自定义setter

假如我们要在ImageView中指定网络图片URL路径来进行显示，显然是不太可能的，但是Data Binding提供了`@BindingAdapter`注解来帮我们实现，它的参数是一个数组，我们先来看看怎么使用！

```xml
<layout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto"
    >

    <data class=".CustomSetterBinding">

        <variable
            name="imageUrl"
            type="String"/>
    </data>


    <ImageView
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:imageUrl="@{imageUrl}"/>

</layout>

```

上面我们自定义了一个`imageUrl`属性来指定图片的Url路径，下面Activity，就一句代码，给它设置值。

```java
public class CustomSetterActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        CustomSetterBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_custom_setter);
        binding.setImageUrl("http://www.liyongqiang.com/wp-content/uploads/2017/05/12-300x300.jpg");
    }
}
```

接下来是最重要的，加载图片这里使用的是Glide（你可以选择自己喜欢的框架），首先我们随便定义一个类；你可以把它当做是工具类。

```java
public class Utils
{
    //    @BindingAdapter({"bind:imageUrl"})
    //    官网文档是上面这样写的，但是编译的时候会有个警告，所以按下面这样写吧...
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String url)
    {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }
}
```

下面是运行结果，长这样...

![.](https://ws1.sinaimg.cn/large/006tNc79gy1fotnjt4yarj30u01hc79h.jpg)

##### 自定义Converters

转换的意思，比如我们要给TextView设置格式化的时间，但是你只有Date变量肿么办？虽然你可以先转换后在进行设置，但是Data Binding也提供了另一个方案，`@BindingConversion`你没有看错，它也是一个注解；我们先来看看它怎么使用吧！

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data class=".ConvertersBinding">

        <variable
            name="date"
            type="java.util.Date"/>
    </data>

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="@{date}"/>
</layout>
```

```java
public class ConvertersActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ConvertersBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_converters);
        binding.setDate(new Date());
    }
}
```

接下来便是时间的转换了，还是随便定义一个工具类，和上面自定义setter一样。

```
public class Utils
{
    @BindingConversion
    public static String converterDate(Date date)
    {
        return new SimpleDateFormat("yyyy-HH-mm hh:MM:ss").format(date);
    }
}
```

下面是Converter ViewBinding中的代码，它是这样来转换的，setter也一样，所以我说它其实就是个工具类。

![.](https://ws2.sinaimg.cn/large/006tNc79gy1fotnwbf08uj31kw0eln08.jpg)

#### 最后

关于Data Binding我还没有运用在项目中，这篇博客写的也有点乱；欢迎各位小伙伴及时指正错误，感谢！

#### 参考

[https://developer.android.google.cn/topic/libraries/data-binding/index.html#converters](https://developer.android.google.cn/topic/libraries/data-binding/index.html#converters)

[http://blog.csdn.net/jdsjlzx/article/details/48133293](http://blog.csdn.net/jdsjlzx/article/details/48133293)
