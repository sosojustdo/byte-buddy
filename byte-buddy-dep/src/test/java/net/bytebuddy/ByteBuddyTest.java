package net.bytebuddy;

import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.test.utility.ObjectPropertyAssertion;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ByteBuddyTest {

    @Test(expected = IllegalArgumentException.class)
    public void testEnumWithoutValuesIsIllegal() throws Exception {
        new ByteBuddy().makeEnumeration();
    }

    @Test
    public void testEnumeration() throws Exception {
        Class<?> type = new ByteBuddy()
                .makeEnumeration("foo")
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        assertThat(Modifier.isPublic(type.getModifiers()), is(true));
        assertThat(type.isEnum(), is(true));
        assertThat(type.isInterface(), is(false));
        assertThat(type.isAnnotation(), is(false));
    }

    @Test
    public void testInterface() throws Exception {
        Class<?> type = new ByteBuddy()
                .makeInterface()
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        assertThat(Modifier.isPublic(type.getModifiers()), is(true));
        assertThat(type.isEnum(), is(false));
        assertThat(type.isInterface(), is(true));
        assertThat(type.isAnnotation(), is(false));
    }

    @Test
    public void testAnnotation() throws Exception {
        Class<?> type = new ByteBuddy()
                .makeAnnotation()
                .make()
                .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
                .getLoaded();
        assertThat(Modifier.isPublic(type.getModifiers()), is(true));
        assertThat(type.isEnum(), is(false));
        assertThat(type.isInterface(), is(true));
        assertThat(type.isAnnotation(), is(true));
    }

    @Test
    public void testImplicitStrategyBootstrap() throws Exception {
        Class<?> type = new ByteBuddy()
                .subclass(Object.class)
                .make()
                .load(ClassLoadingStrategy.BOOTSTRAP_LOADER)
                .getLoaded();
        assertThat(type.getClassLoader(), notNullValue(ClassLoader.class));
    }

    @Test
    public void testImplicitStrategyNonBootstrap() throws Exception {
        ClassLoader classLoader = new URLClassLoader(new URL[0], null);
        Class<?> type = new ByteBuddy()
                .subclass(Object.class)
                .make()
                .load(classLoader)
                .getLoaded();
        assertThat(type.getClassLoader(), is(classLoader));
    }

    @Test
    public void testObjectProperties() throws Exception {
        ObjectPropertyAssertion.of(ByteBuddy.class).apply();
        ObjectPropertyAssertion.of(ByteBuddy.EnumerationImplementation.class).apply();
    }
}
