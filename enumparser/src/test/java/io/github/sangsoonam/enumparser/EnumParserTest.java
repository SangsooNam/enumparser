package io.github.sangsoonam.enumparser;

import org.junit.Test;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class EnumParserTest {

    enum TestEnum { Bar, FOO_BAR }

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
    public void testParseWithVariations() {
        assertThat(EnumParser.forClass(TestEnum.class).parse("FOO-BAR")).isNull();

        final EnumParser<TestEnum> enumParser = EnumParser.forClass(TestEnum.class).withVariation(new Function<Enum<TestEnum>, String>() {
            @Override
            public String apply(Enum<TestEnum> testEnum) {
                return testEnum.name().replaceAll("_", "-");
            }
        });
        assertThat(enumParser.parse("FOO-BAR")).isSameAs(TestEnum.FOO_BAR);
        assertThat(enumParser.parse("FOO_BAR")).isSameAs(TestEnum.FOO_BAR);
    }

}