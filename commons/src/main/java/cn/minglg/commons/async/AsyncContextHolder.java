package cn.minglg.commons.async;

/**
 * ClassName:AsyncContextHolder
 * Package:cn.minglg.commons.async
 * Description:
 *
 * @Author kfzx-minglg
 * @Create 2025/12/20
 * @Version 1.0
 */
public class AsyncContextHolder {
    private static final ThreadLocal<AsyncContext> contextHolder = ThreadLocal.withInitial(() -> null);

    /**
     * 获取当前线程的异步上下文对象
     *
     * @return 返回当前线程绑定的AsyncContext实例，如果未设置则可能返回null
     */
    public static AsyncContext getContext() {
        return contextHolder.get();
    }


    /**
     * 设置异步上下文对象
     *
     * @param context 异步上下文对象，如果为null则清除当前上下文
     */
    public static void setContext(AsyncContext context) {
        if (context == null) {
            // 传入参数为空时，说明不需要设置上下文对象，清除当前线程的上下文
            clearContext();
        } else {
            // 将上下文对象存储到线程局部变量中
            contextHolder.set(context);
        }
    }


    /**
     * 根据指定的键获取属性值
     *
     * @param <T> 属性值的泛型类型
     * @param key 属性键，用于查找对应的属性值
     * @return 返回与指定键关联的属性值，如果上下文不存在或属性不存在则返回null
     */
    public static <T> T getAttribute(String key) {
        // 获取当前异步上下文
        AsyncContext context = getContext();
        // 如果上下文存在则获取属性值，否则返回null
        return context == null ? null : context.getAttribute(key);
    }


    /**
     * 设置异步上下文中的属性值
     *
     * @param key   属性键名，用于标识要设置的属性
     * @param value 属性值，可以是任意类型的对象
     * @param <T>   泛型类型参数，表示属性值的类型
     */
    public static <T> void setAttribute(String key, T value) {
        // 获取当前线程的异步上下文
        AsyncContext context = getContext();
        // 如果上下文不存在，则创建新的上下文并设置到当前线程
        if (context == null) {
            context = new AsyncContext();
            setContext(context);
        }
        // 在上下文中设置指定键值对
        context.setAttribute(key, value);
    }


    /**
     * 清除当前线程的异步上下文环境
     * 该方法用于清理当前线程中存储的异步上下文信息，包括：
     * 1. 获取当前线程的异步上下文实例
     * 2. 如果上下文存在，则清空其内部数据
     * 3. 从线程局部变量中移除上下文引用
     */
    public static void clearContext() {
        // 获取当前线程的异步上下文
        AsyncContext context = getContext();
        if (context != null) {
            // 清空上下文中的数据
            context.clear();
        }
        // 从线程局部变量中移除上下文引用
        contextHolder.remove();
    }
}
