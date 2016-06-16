package tw.yalan.eyedow;

import android.support.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Alan Ding on 2016/6/14.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Layout {
    /**
     * Eyedow container's layout resource id.
     *
     * @return
     */
    @LayoutRes int id() default 0;

    /**
     * Eyedow default width when show.
     * Base on dp.
     *
     * @return
     */
    int width() default -1;

    /**
     * Eyedow default height when show.
     * Base on dp.
     *
     * @return
     */
    int height() default -1;

}
