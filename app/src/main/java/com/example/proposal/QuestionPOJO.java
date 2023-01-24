package com.example.proposal;

public class QuestionPOJO {

    private long qid;

    private String testname;

    private String hint;

    public String description;

    private String solution;

    private String answer;

    private String category;

    public void setQid(long qid) {
        this.qid = qid;
    }

    public void setTestname(String testname) {
        this.testname = testname;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTest(TestPOJO test) {
        this.test = test;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public long getQid() {
        return qid;
    }

    public String getTestname() {
        return testname;
    }

    public String getHint() {
        return hint;
    }

    public String getDescription() {
        return description;
    }

    public String getSolution() {
        return solution;
    }

    public String getAnswer() {
        return answer;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public TestPOJO getTest() {
        return test;
    }

    public String getScore() {
        return score;
    }

    private String image;

    private TestPOJO test;

    private String score;

    public QuestionPOJO(String name, String hint, String description, String solution, String category) {
        this.testname = name;
        this.hint = hint;
        this.description = description;
        this.solution = solution;
        this.category = category;
    }
    public String getName() {
        return testname;
    }

}
