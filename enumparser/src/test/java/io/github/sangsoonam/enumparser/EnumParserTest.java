package io.github.sangsoonam.enumparser;

import com.google.common.base.Function;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class EnumParserTest {

    enum TestEnum { Bar, FOO_BAR }

    enum TestCustomKeyEnum {
        FOO("hello:foo"),
        BAR("hello:bar");

        private final String mKey;

        TestCustomKeyEnum(String key) {
            mKey = key;
        }

        public String getKey() {
            return mKey;
        }
    }

    @Test
    public void testParseWhenDifferentCase() {
        assertThat(EnumParser.forClass(TestEnum.class).parse("Bar")).isSameAs(TestEnum.Bar);
        assertThat(EnumParser.forClass(TestEnum.class).parse("BAR")).isSameAs(TestEnum.Bar);
        assertThat(EnumParser.forClass(TestEnum.class).parse("bar")).isSameAs(TestEnum.Bar);
    }


    @Test
    public void testParseStrictWhenDifferentCase() {
        assertThat(EnumParser.forClass(TestEnum.class).parseStrict("Bar")).isSameAs(TestEnum.Bar);
        assertThat(EnumParser.forClass(TestEnum.class).parseStrict("BAR")).isSameAs(TestEnum.Bar);
        assertThat(EnumParser.forClass(TestEnum.class).parseStrict("bar")).isSameAs(TestEnum.Bar);
    }

    @Test
    public void testParseWhenNull() {
        assertThat(EnumParser.forClass(TestEnum.class).parse(null)).isNull();
    }

    @Test(expected = EnumConstantNotPresentException.class)
    public void testParseStrictWhenNull() {
        assertThat(EnumParser.forClass(TestEnum.class).parseStrict(null)).isNull();
    }

    @Test
    public void testParseWhenConstantNotPresent() {
        assertThat(EnumParser.forClass(TestEnum.class).parse("NEW")).isNull();
    }

    @Test(expected = EnumConstantNotPresentException.class)
    public void testParseStrictWhenConstantNotPresent() {
        assertThat(EnumParser.forClass(TestEnum.class).parseStrict("NEW")).isNull();
    }

    @Test
    public void testParseWithDashVariation() {
        assertThat(EnumParser.forClass(TestEnum.class).parse("FOO-BAR")).isNull();

        final Function<TestEnum, String> dashVariation = new Function<TestEnum, String>() {
            @Override
            public String apply(TestEnum testEnum) {
                return testEnum.name().replaceAll("_", "-");
            }
        };
        final EnumParser<TestEnum> enumParser = EnumParser.forClass(TestEnum.class).withVariation(dashVariation);
        assertThat(enumParser.parse("FOO-BAR")).isSameAs(TestEnum.FOO_BAR);
        assertThat(enumParser.parse("FOO_BAR")).isSameAs(TestEnum.FOO_BAR);
    }

    @Test
    public void testParseWithCustomKeyVariation() {
        final Function<TestCustomKeyEnum, String> customKeyVariation = new Function<TestCustomKeyEnum, String>() {
            @Override
            public String apply(TestCustomKeyEnum testEnum) {
                return testEnum.getKey();
            }
        };
        final EnumParser<TestCustomKeyEnum> enumParser = EnumParser.forClass(TestCustomKeyEnum.class).withVariation(customKeyVariation);
        assertThat(enumParser.parse("hello:foo")).isSameAs(TestCustomKeyEnum.FOO);
        assertThat(enumParser.parse("hello:bar")).isSameAs(TestCustomKeyEnum.BAR);
    }

}