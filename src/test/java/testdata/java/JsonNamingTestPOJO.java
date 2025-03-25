package testdata.java;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
class LowerCamelCaseStrategyTestPOJO {
    private String firstName;
}

@JsonNaming(PropertyNamingStrategies.UpperCamelCaseStrategy.class)
class UpperCamelCaseStrategyTestPOJO {
    private String firstName;
}

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
class SnakeCaseStrategyTestPOJO {
    private String firstName;
}

@JsonNaming(PropertyNamingStrategies.UpperSnakeCaseStrategy.class)
class UpperSnakeCaseStrategyTestPOJO {
    private String firstName;
}

@JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
class KebabCaseStrategyTestPOJO {
    private String firstName;
}

@JsonNaming(PropertyNamingStrategies.LowerCaseStrategy.class)
class LowerCaseStrategyTestPOJO {
    private String firstName;
}

@JsonNaming(PropertyNamingStrategies.LowerDotCaseStrategy.class)
class LowerDotCaseStrategyTestPOJO {
    private String firstName;
}