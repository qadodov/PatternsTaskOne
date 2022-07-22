package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class DeliveryTest {

    @BeforeEach
    void setUp() {
        Configuration.browserSize = "1280x720";
        Configuration.browser = "chrome";
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfullyPlanAndReplanMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        int daysToAddForFirstMeeting = 4;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        int daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);

        $x("//*[@data-test-id=\"city\"]//input").setValue(validUser.getCity());
        $x("//*[@placeholder=\"Дата встречи\"]").setValue(firstMeetingDate);
        $x("//*[@name=\"name\"]").setValue(validUser.getName());
        $x("//*[@name=\"phone\"]").setValue(validUser.getPhone());
        $x("//*[@data-test-id=\"agreement\"]").click();
        $x("//span[text()=\"Запланировать\"]/../parent::button").click();
        $x("//div[contains(text(), \"Встреча успешно запланирована на\")]").shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate), Duration.ofSeconds(15));
        $x("//*[@placeholder=\"Дата встречи\"]").setValue(secondMeetingDate);
        $x("//span[text()=\"Запланировать\"]/../parent::button").click();
        $$x("//*[@class=\"notification__content\"]").find(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $x("//*[text()=\"Перепланировать\"]/../parent::button").should(Condition.appear, Duration.ofSeconds(15)).click();
        $x("//*[@class=\"notification__content\"]").shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }
}