package io.github.sangsoonam.enumparser;

import com.google.common.base.Function;
import com.google.common.collect.Maps;

import java.util.Locale;
import java.util.Map;

/**
 * Utility for parsing a string to Enum constant. This is case insensitive.
 *
 * @param <T> enum class
 */
public final class EnumParser<T extends Enum<T>> {

    private final Class<T> mEnumClass;
    private final Map<String, T> mMapping;
    private final T[] mEnumConstants;

    /**
     * Generate EnumParser
     *
     * @param enumClass enum class
     * @param <T>       enum class
     * @return EnumParserfor the enum class
     */
    public static <T extends Enum<T>> EnumParser<T> forClass(Class<T> enumClass) {
        return new EnumParser<>(enumClass);
    }

    private EnumParser(Class<T> enumClass) {
        mEnumClass = enumClass;
        mEnumConstants = enumClass.getEnumConstants();
        mMapping = Maps.newHashMapWithExpectedSize(mEnumConstants.length);
        for (T enumConstant : mEnumConstants) {
            mMapping.put(toUpperCase(enumConstant.name()), enumConstant);
        }
    }

    /**
     * Make a key variation using an input function
     * @param variation variation function
     * @return EnumParser
     */
    public EnumParser<T> withVariation(Function<T, String> variation) {
        for (T enumConstant : mEnumConstants) {
            mMapping.put(toUpperCase(variation.apply(enumConstant)), enumConstant);
        }
        return this;
    }

    /**
     * Parse a key string and return a matched Enum value
     *
     * @param fromString input string
     * @return Enum value if it is found. Otherwise, null
     */
    public T parse(String fromString) {
        if (fromString == null) {
            return null;
        }
        return mMapping.get(toUpperCase(fromString));
    }

    /**
     * Parse a key string and return a matched Enum value
     *
     * @param fromString input string
     * @return Enum value if it is found. Otherwise, throw {@link EnumConstantNotPresentException}
     */
    public T parseStrict(String fromString) {
        if (fromString == null) {
            throw new EnumConstantNotPresentException(mEnumClass, fromString);
        }
        T t = mMapping.get(toUpperCase(fromString));
        if (t == null) {
            throw new EnumConstantNotPresentException(mEnumClass, fromString);
        }
        return t;
    }

    /**
     * Convert a string to uppercase
     *
     * @param fromString input string
     * @return upper case string
     */
    private static String toUpperCase(String fromString) {
        return fromString.toUpperCase(Locale.US);
    }
}