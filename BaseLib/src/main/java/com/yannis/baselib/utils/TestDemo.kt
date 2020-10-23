package com.yannis.baselib.utils

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/10/22
 */
class TestDemo private constructor() {

    companion object {
        val instance: TestDemo = Holder.holer

    }

    private object Holder {
        val holer = TestDemo()
    }

    var name: String = "战三"
    var age: Int = 34
}
/**
 *
 *  class TestDemo{
 *     var name:String="战三"
 *     var age:Int = 34
 * }
 * --------- --------- --------- ---------
 * public final class TestDemo {
 *    @NotNull
 *    private String name = "战三";
 *    private int age = 34;
 *
 *    @NotNull
 *    public final String getName() {
 *       return this.name;
 *    }
 *
 *    public final void setName(@NotNull String var1) {
 *       Intrinsics.checkNotNullParameter(var1, "<set-?>");
 *       this.name = var1;
 *    }
 *
 *    public final int getAge() {
 *       return this.age;
 *    }
 *
 *    public final void setAge(int var1) {
 *       this.age = var1;
 *    }
 * }
 * ========== ========== ==========
 * class TestDemo{
 *     val name:String="战三"
 *     val age:Int = 34
 * }
 *
 * --------- --------- --------- ---------
 * public final class TestDemo {
 *    @NotNull
 *    private final String name = "战三";
 *    private final int age = 34;
 *
 *    @NotNull
 *    public final String getName() {
 *       return this.name;
 *    }
 *
 *    public final int getAge() {
 *       return this.age;
 *    }
 * }
 *
 *  ========== ========== ==========
 * class TestDemo private constructor(){
 *     var name:String="战三"
 *     var age:Int = 34
 * }
 * --------- --------- --------- ---------
 *  public final class TestDemo {
 *    @NotNull
 *    private String name = "战三";
 *    private int age = 34;
 *
 *    @NotNull
 *    public final String getName() {
 *       return this.name;
 *    }
 *
 *    public final void setName(@NotNull String var1) {
 *       Intrinsics.checkNotNullParameter(var1, "<set-?>");
 *       this.name = var1;
 *    }
 *
 *    public final int getAge() {
 *       return this.age;
 *    }
 *
 *    public final void setAge(int var1) {
 *       this.age = var1;
 *    }
 *
 *    private TestDemo() {
 *    }
 * }
 *
 *  ========== ========== ==========
 * // object 修饰的类为"单例"，而非"静态类"，静态方法应该使用"@JvmStatic"
 * // object的用途：对象表达式（创建匿名对象）/对象声明（单例）
 * // 官方：https://www.kotlincn.net/docs/reference/object-declarations.html
 * object TestDemo{
 *     val name:String="战三"
 *     val age:Int = 34
 * }
 * --------- --------- --------- ---------
 * public final class TestDemo {
 *    @NotNull
 *    private static final String name;
 *    private static final int age;
 *    public static final TestDemo INSTANCE;
 *
 *    @NotNull
 *    public final String getName() {
 *       return name;
 *    }
 *
 *    public final int getAge() {
 *       return age;
 *    }
 *
 *    private TestDemo() {
 *    }
 *
 *    static {
 *       TestDemo var0 = new TestDemo();
 *       INSTANCE = var0;
 *       name = "战三";
 *       age = 34;
 *    }
 * }
 *
 *  ========== ========== ==========
 *
 * class TestDemo private constructor() {
 *
 *     companion object {
 *         val instance: TestDemo by lazy {
 *             TestDemo()
 *         }
 *     }
 *
 *     var name: String = "战三"
 *     var age: Int = 34
 * }
 * --------- --------- --------- ---------
 * public final class TestDemo {
 *    @NotNull
 *    private String name;
 *    private int age;
 *    @NotNull
 *    private static final Lazy instance$delegate;
 *    public static final TestDemo.Companion Companion = new TestDemo.Companion((DefaultConstructorMarker)null);
 *
 *    @NotNull
 *    public final String getName() {
 *       return this.name;
 *    }
 *
 *    public final void setName(@NotNull String var1) {
 *       Intrinsics.checkNotNullParameter(var1, "<set-?>");
 *       this.name = var1;
 *    }
 *
 *    public final int getAge() {
 *       return this.age;
 *    }
 *
 *    public final void setAge(int var1) {
 *       this.age = var1;
 *    }
 *
 *    private TestDemo() {
 *       this.name = "战三";
 *       this.age = 34;
 *    }
 *
 *    static {
 *       instance$delegate = LazyKt.lazy((Function0)null.INSTANCE);
 *    }
 *
 *    // $FF: synthetic method
 *    public TestDemo(DefaultConstructorMarker $constructor_marker) {
 *       this();
 *    }
 *
 *    public static final class Companion {
 *       @NotNull
 *       public final TestDemo getInstance() {
 *          Lazy var1 = TestDemo.instance$delegate;
 *          TestDemo.Companion var2 = TestDemo.Companion;
 *          Object var3 = null;
 *          boolean var4 = false;
 *          return (TestDemo)var1.getValue();
 *       }
 *
 *       private Companion() {
 *       }
 *
 *       // $FF: synthetic method
 *       public Companion(DefaultConstructorMarker $constructor_marker) {
 *          this();
 *       }
 *    }
 * }
 *  ========== ========== ==========
 * class TestDemo private constructor() {
 *
 *     companion object {
 *         val instance: TestDemo = Holder.holer
 *     }
 *
 *     private object Holder{
 *         val holer=TestDemo()
 *     }
 *
 *     var name: String = "战三"
 *     var age: Int = 34
 * }
 * --------- --------- --------- ---------
 * public final class TestDemo {
 *    @NotNull
 *    private String name;
 *    private int age;
 *    @NotNull
 *    private static final TestDemo instance;
 *    public static final TestDemo.Companion Companion = new TestDemo.Companion((DefaultConstructorMarker)null);
 *
 *    @NotNull
 *    public final String getName() {
 *       return this.name;
 *    }
 *
 *    public final void setName(@NotNull String var1) {
 *       Intrinsics.checkNotNullParameter(var1, "<set-?>");
 *       this.name = var1;
 *    }
 *
 *    public final int getAge() {
 *       return this.age;
 *    }
 *
 *    public final void setAge(int var1) {
 *       this.age = var1;
 *    }
 *    //- - - 主类-私有构造函数
 *    private TestDemo() {
 *       this.name = "战三";
 *       this.age = 34;
 *    }
 *
 *    //- - - 主类-静态对象
 *    static {
 *       instance = TestDemo.Holder.INSTANCE.getHoler();
 *    }
 *
 *    // $FF: synthetic method
 *    public TestDemo(DefaultConstructorMarker $constructor_marker) {
 *       this();
 *    }
 *    //- - - 静态内部类
 *    private static final class Holder {
 *       @NotNull
 *       private static final TestDemo holer;
 *       public static final TestDemo.Holder INSTANCE;
 *
 *       @NotNull
 *       public final TestDemo getHoler() {
 *          return holer;
 *       }
 *
 *       static {
 *          TestDemo.Holder var0 = new TestDemo.Holder();
 *          INSTANCE = var0;
 *          holer = new TestDemo((DefaultConstructorMarker)null);
 *       }
 *    }
 *
 *    //- - - 伴生类
 *    public static final class Companion {
 *       @NotNull
 *       public final TestDemo getInstance() {
 *          return TestDemo.instance;
 *       }
 *
 *       private Companion() {
 *       }
 *
 *       // $FF: synthetic method
 *       public Companion(DefaultConstructorMarker $constructor_marker) {
 *          this();
 *       }
 *    }
 * }
 */

