package data;

public class PairQnA {

    private final String content;
    private final String answer;

    public PairQnA(String content, String answer) {
        this.content = content;
        this.answer = answer;
    }

    public String getContent() {
        return content;
    }

    public String getAnswer() {
        return answer;
    }
}
