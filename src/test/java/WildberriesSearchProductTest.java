
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class WildberriesSearchProductTest {

    @BeforeEach
    void setUp() {
        open("https://www.wildberries.ru");

    }
    @Tag("SMOKE")
    @ValueSource(strings = {
            "Телевизор", "Диван"
    })

    @ParameterizedTest(name = "Для поискового запроса {0} должно выводится карточки объявлений с товаром")
    void testProductSearch(String searchQuery) {
        $("#searchInput").setValue(searchQuery).pressEnter();
        $$(".searching-results__title")
                .shouldHave(sizeGreaterThan(0));

    }

    @ParameterizedTest(name = "Поиск по запросу ''{0}'' (англ. раскладка) должен находить ''{1}''")
    @MethodSource("searchQueriesProvider")
    void testSearchWithEnglishLayoutMethod(String englishInput, String expectedResult) {
        $("#searchInput").setValue(englishInput).pressEnter();
        $$(".product-card").shouldHave(sizeGreaterThan(0));
        $$(".product-card").first()
                .shouldHave(text(expectedResult));
    }

    static Stream<Arguments> searchQueriesProvider() {
        return Stream.of(
                Arguments.of("ntktdbpjh", "Телевизор"),
                Arguments.of("lbdfy", "Диван")
        );
    }


    @ParameterizedTest(name = "Поиск {0} должен возвращать результаты")
    @CsvSource({
            "футболка мужская, Футболка мужская",
            "футболка женская, Футболка женская",
            "футболка детская, Футболка детская"
    })
    void testTShirtSearchByType(String searchQuery, String expectedResult) {

        $("#searchInput")
                .setValue(searchQuery)
                .pressEnter();
        $(".searching-results__title")
                .shouldHave(text(expectedResult));

    }

}
