## EnumParser
[![Build Status](https://travis-ci.org/SangsooNam/enumparser.svg?branch=master)](https://travis-ci.org/SangsooNam/enumparser)
[![Coverage Status](https://coveralls.io/repos/github/SangsooNam/enumparser/badge.svg?branch=master)](https://coveralls.io/github/SangsooNam/enumparser?branch=master)

This is a utility for parsing a string to Enum constant. One common case is to parse a JSON data returned from a backend. In that case, you might want to convert it to Enum because writing below code in many places could bring a mistake because of a typo error.
```java
"play".isEquals(modeString);
```

In order to convert, `valueOf` is widely used.
```java
enum Mode {
  PLAY,
  PAUSE,
  SHUFFLE_PLAY
};

...

Mode mode = Mode.valueOf(modeString);
```

It works well in most cases. However, there are two problems. First, it is a case sensitive. If `modeString` is `play`, it returns `null` instead of `PLAY`. EnumParser support a case insensitive mapping. Thus, belows will match the same enum constant `PLAY`.

```java
EnumParser<Mode> parser = EnumParser.forClass(Mode.clasS);
assertEquals(Mode.PLAY, parser.parser("play"));
assertEquals(Mode.PLAY, parser.parser("Play"));
assertEquals(Mode.PLAY, parser.parser("PLAY"));
```

Second, it doesn't support variations. One variation is about `-`. Enum doens't allow to have a `-` as a part of constant. `_` is used intead of that. To make `valueOf` work, a backend should return `modeString` with `_` even though `-` is more relevant in the backend side. Otherwise, we need to have this logic everytime.
```java
Mode mode = Mode.valueOf(modeString.replaceAll("-", "_");
```

Moreover, some enums have a custom key, which can be used to parse. Unfortunately, `valueOf` only cares about its name. Finding a enum constant using its key is not that easy. 
```java
enum ModeWithKey {
  PLAY("music:play"),
  PAUSE("music:pause");

  private final String mKey;

  ModeWithKey(String key) {
    mKey = key;
  }

  public String getKey() {
    return mKey;
  }
}
```

EnumParser supports variations so both `-` and a custom key would be easily solved. 
```java
Function<Mode, String> dashVariation = new Function<Mode, String>() {
  @Override
  public String apply(Mode testEnum) {
    return testEnum.name().replaceAll("_", "-");
  }
};
EnumParser<Mode> enumParser = EnumParser.forClass(Mode).withVariation(dashVariation);
assertEquals(Mode.SHUFFLE_PLAY, enumParser.parse("shuffle-play"));
```
```java

Function<ModeWithKey, String> customKeyVariation = new Function<ModeWithKey, String>() {
  @Override
  public String apply(ModeWithKey testEnum) {
    return testEnum.getKey();
  }
};
EnumParser<ModeWithKey> enumParser = EnumParser.forClass(ModeWithKey).withVariation(customKeyVariation);
assertEquals(Mode.PLAY, enumParser.parse("music:play"));
```


## Licene
Licensed under the MIT License.
