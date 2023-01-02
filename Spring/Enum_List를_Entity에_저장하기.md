### **도입 계기**
앱을 새로 리뉴얼하면서 바뀌어야 하는 부분 중에서, 하나의 그룹에서 하나의 식사 타입을 가지고 있는 구조에서 **하나의 그룹에서 여러개 식사 타입**을 가져야하는 상황이 발생했다. 식사 타입은 Enum 값으로, `@Converter`를 사용하여 Entity의 컬럼에 추가되어있는 구조였다.

```java
@Entity
public class Group {
    ...
    @NotNull
    @Convert(converter = DiningTypeConverter.class)
    @Column(nullable = false)
    @Comment("식사 타입")
    private DiningType diningType;
    ...
}
```
하지만 하나의 그룹에서 여러가지의 식사 타입(아침, 점심, 저녁)을 가지고 와야하는 상황이 발생했고, 종류가 3가지 밖에 되지 않는 Enum 값에서 `@OneToMany`로 연관관계를 맺고 싶지 않았다. 그래서 생각해본 것이 **현재 구현되어 있는 구조를 깨지 않고 저장하는 방법은 없을까?**라는 생각에 도입하게 되었다.

### **AttributeConverter?**
AttributeConverter은 주로 다음과 같은 상황에서 사용한다.
- JPA가 지원하지 않는 타입을 매핑 
- 두 개 이상의 속성을 갖는 타입을 한 개 컬럼에 매핑

AttributeConverter를 사용하여 Enum을 저장하는 부분은 <a href= "https://minji6119.tistory.com/42">여기</a>를 참고하면 된다. 

> AttributeConverter<X, Y> 의
X는 Entity Attribute
Y는 Database Column이며, Y에는 기본 타입(int, double)이 사용 불가능 하다.

### **List<Enum>을 Entity 테이블에 저장하기**

**DiningType**
```java
@Getter
public enum DiningType {
    NULL("선택안함", 0),
    MORNING("아침", 1),
    LUNCH("점심", 2),
    DINNER("저녁", 3);

    private final String diningType;
    private final Integer code;

    DiningType(String diningType, Integer code) {
        this.diningType = diningType;
        this.code = code;
    }

    public static DiningType ofCode(Integer code) {
        return Arrays.stream(DiningType.values())
                .filter(v -> v.getCode().equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 식사 타입입니다."));
    }
}
```
String 값과 데이터베이스에 저장할 Integer 값만 존재하는 Enum이다.<br>

**DiningTypesConverter**
```java
@Converter
public class DiningTypesConverter implements AttributeConverter<List<DiningType>, String> {
    private static final String SEPARATOR = ",";

    @Override
    public String convertToDatabaseColumn(List<DiningType> diningTypes) {
        if (diningTypes == null || diningTypes.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (DiningType diningType : diningTypes) {
            sb.append(diningType.getCode()).append(SEPARATOR);
        }

        // remove the last separator
        sb.setLength(sb.length() - SEPARATOR.length());

        return sb.toString();
    }

    @Override
    public List<DiningType> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        String[] diningTypeStrings = dbData.split(SEPARATOR);
        List<DiningType> diningTypes = new ArrayList<>();

        for(String diningTypeString : diningTypeStrings) {
            diningTypes.add(DiningType.ofCode(Integer.parseInt(diningTypeString)));
        }
        return diningTypes;
    }
}
```
데이터베이스에서는 아침, 점심 이라면 **1,2**와 같은 식으로 저장해두고 싶었기 때문에 SEPARATOR를 ","로 두었다.

```java
public class Group {
    ...
    @NotNull
    @Convert(converter = DiningTypesConverter.class)
    @Column(nullable = false)
    @Comment("식사 타입")
    private List<DiningType> diningType;
    ...
}
```
이런식으로 구현하면 완료!