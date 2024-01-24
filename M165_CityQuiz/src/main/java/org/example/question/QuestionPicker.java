package org.example.question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class QuestionPicker {

    public static void displayRandomQuestion() {
        String[] categories = {"population", "populationDensity", "area", "avgAge", "gdpPpp"};
        QuestionType[] questionTypes = QuestionType.values();

        Random random = new Random();

        String category = categories[random.nextInt(categories.length)];
        QuestionType questionType = questionTypes[random.nextInt(questionTypes.length)];


        String question = generateQuestionString(category, questionType);

        new Question(question, questionType, category);
    }

    private static String generateQuestionString(String category, QuestionType questionType) {
        switch (category) {
            case "population":
                switch (questionType) {
                    case MOST:
                        return "Which city has the highest population?";
                    case MORE:
                        return "Which city has a higher population?";
                    case LESS:
                        return "Which city has a lower population?";
                    case LEAST:
                        return "Which city has the lowest population?";
                }
                break;

            case "populationDensity":
                switch (questionType) {
                    case MOST:
                        return "Which city has the highest population density?";
                    case MORE:
                        return "Which city has a higher population density?";
                    case LESS:
                        return "Which city has a lower population density?";
                    case LEAST:
                        return "Which city has the lowest population density?";
                }
                break;

            case "area":
                switch (questionType) {
                    case MOST:
                        return "Which city has the largest area?";
                    case MORE:
                        return "Which city has a larger area?";
                    case LESS:
                        return "Which city has a smaller area?";
                    case LEAST:
                        return "Which city has the smallest area?";
                }
                break;

            case "avgAge":
                switch (questionType) {
                    case MOST:
                        return "Which city has the highest average age?";
                    case MORE:
                        return "Which city has a higher average age?";
                    case LESS:
                        return "Which city has a lower average age?";
                    case LEAST:
                        return "Which city has the lowest average age?";
                }
                break;

            case "gdpPpp":
                switch (questionType) {
                    case MOST:
                        return "Which city has the highest GDP (PPP)?";
                    case MORE:
                        return "Which city has a higher GDP (PPP)?";
                    case LESS:
                        return "Which city has a lower GDP (PPP)?";
                    case LEAST:
                        return "Which city has the lowest GDP (PPP)?";
                }
                break;
        }
        return "Invalid question";
    }

}
