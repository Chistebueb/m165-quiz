package org.example.question;

import org.example.App;

import java.util.List;
import java.util.Random;

public class QuestionPicker {

    public static void displayRandomQuestion() {
        List<String> allCategories = App.getDb().getAllCategories();
        String[] categories = allCategories.toArray(new String[0]);
        QuestionType[] questionTypes = QuestionType.values();

        Random random = new Random();


        String category = categories[random.nextInt(categories.length)];
        QuestionType questionType = questionTypes[random.nextInt(questionTypes.length)];


        String question = generateQuestionString(category, questionType);

        new Question(question, questionType, category);
    }

    private static String generateQuestionString(String category, QuestionType questionType) {
        return App.getDb().getQuestion(category, questionType);
    }

}
