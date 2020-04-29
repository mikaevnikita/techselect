package ru.stankin.mikaev.techselect.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stankin.mikaev.techselect.dto.AnswerDto;
import ru.stankin.mikaev.techselect.dto.QuestionDto;
import ru.stankin.mikaev.techselect.exception.QuestionDoesNotContainsAnswerException;
import ru.stankin.mikaev.techselect.model.Answer;
import ru.stankin.mikaev.techselect.model.AnswerMeta;
import ru.stankin.mikaev.techselect.model.Question;
import ru.stankin.mikaev.techselect.model.QuestionMeta;
import ru.stankin.mikaev.techselect.repository.AnswerMetaRepository;
import ru.stankin.mikaev.techselect.repository.AnswerRepository;
import ru.stankin.mikaev.techselect.repository.QuestionMetaRepository;
import ru.stankin.mikaev.techselect.repository.QuestionRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SurveyService.
 *
 * @author Nikita_Mikaev
 */
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final QuestionMetaRepository questionMetaRepository;
    private final AnswerMetaRepository answerMetaRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    private static final Long SESSION_ID = 1L;

    private AnswerDto mapToAnswer(AnswerMeta answerMeta) {
        return AnswerDto.builder()
                .id(answerMeta.getId())
                .text(answerMeta.getText())
                .build();
    }

    private List<AnswerDto> mapToAnswers(List<AnswerMeta> answerMetaList) {
        return answerMetaList.stream()
                .map(this::mapToAnswer)
                .collect(Collectors.toList());
    }

    private QuestionDto buildQuestionWithAnswers(QuestionMeta questionMeta, List<AnswerMeta> answerMetas) {
        return QuestionDto
                .builder()
                .id(questionMeta.getId())
                .text(questionMeta.getText())
                .answers(mapToAnswers(answerMetas))
                .build();
    }

    public Optional<QuestionDto> getNextQuestion(QuestionDto questionDto) {
        if (questionDto == null) {
            Optional<Question> lastQuestion = questionRepository.findLastQuestion(SESSION_ID);
            if (lastQuestion.isPresent()) {
                Long lastQuestionMetaId = lastQuestion.get().getMetaId();
                Optional<QuestionMeta> nextQuestionMetaOpt = questionMetaRepository.findById(lastQuestionMetaId + 1);
                if (nextQuestionMetaOpt.isPresent()) {
                    QuestionMeta nextQuestionMeta = nextQuestionMetaOpt.get();
                    List<AnswerMeta> questionAnswers =
                            answerMetaRepository.findAllByQuestionMetaId(nextQuestionMeta.getId());
                    Answer selectedAnswer = answerRepository.findBySessionIdAndQuestionMetaId(SESSION_ID,
                            nextQuestionMeta.getId());
                    QuestionDto resultQuestionDto = buildQuestionWithAnswers(nextQuestionMeta, questionAnswers);
                    if (selectedAnswer != null) {
                        resultQuestionDto.setSelectedAnswer(selectedAnswer.getAnswerMetaId());
                    }
                    return Optional.of(resultQuestionDto);
                } else {
                    return Optional.empty();
                }
            }
            //return first question
            QuestionMeta question = questionMetaRepository.findFirstQuestion();
            List<AnswerMeta> questionAnswers = answerMetaRepository.findAllByQuestionMetaId(question.getId());
            return Optional.of(buildQuestionWithAnswers(question, questionAnswers));
        } else {
            Optional<QuestionMeta> nextQuestionMetaOpt = questionMetaRepository.findById(questionDto.getId() + 1);
            if (nextQuestionMetaOpt.isPresent()) {
                QuestionMeta nextQuestionMeta = nextQuestionMetaOpt.get();
                List<AnswerMeta> questionAnswers =
                        answerMetaRepository.findAllByQuestionMetaId(nextQuestionMeta.getId());
                Answer selectedAnswer = answerRepository.findBySessionIdAndQuestionMetaId(SESSION_ID,
                        nextQuestionMeta.getId());
                QuestionDto resultQuestionDto = buildQuestionWithAnswers(nextQuestionMeta, questionAnswers);
                if (selectedAnswer != null) {
                    resultQuestionDto.setSelectedAnswer(selectedAnswer.getAnswerMetaId());
                }
                return Optional.of(resultQuestionDto);
            }
        }
        return Optional.empty();
    }

    public Optional<QuestionDto> getPrevQuestion(QuestionDto currentQuestion) {
        Optional<Question> lastQuestion = questionRepository.findBySessionIdAndMetaId(SESSION_ID,
                currentQuestion.getId() - 1);
        if (lastQuestion.isPresent()) {
            Question question = lastQuestion.get();
            QuestionMeta questionMeta = questionMetaRepository.findById(question.getMetaId()).get();
            List<AnswerMeta> questionAnswers = answerMetaRepository.findAllByQuestionMetaId(questionMeta.getId());
            Answer selectedAnswer = answerRepository.findBySessionIdAndQuestionMetaId(SESSION_ID, questionMeta.getId());
            QuestionDto questionDto = buildQuestionWithAnswers(questionMeta, questionAnswers);
            questionDto.setSelectedAnswer(selectedAnswer.getAnswerMetaId());
            return Optional.of(questionDto);
        }
        return Optional.empty();
    }

    private void checkIsAssignable(QuestionDto questionDto, AnswerDto answerDto) {
        AnswerMeta answerMeta = answerMetaRepository.findById(answerDto.getId()).get();
        if (answerMeta.getQuestionMetaId() != questionDto.getId()) {
            throw new QuestionDoesNotContainsAnswerException();
        }
    }

    @Transactional
    public void sumbitAnswer(QuestionDto questionDto, AnswerDto answerDto) {
        checkIsAssignable(questionDto, answerDto);
        Answer answer = answerRepository.findBySessionIdAndQuestionMetaId(SESSION_ID, questionDto.getId());
        if (answer != null) {
            answer.setAnswerMetaId(answerDto.getId());
            answer.setText(answerDto.getText());
        } else {
            Question question = Question.builder()
                    .metaId(questionDto.getId())
                    .sessionId(SESSION_ID)
                    .text(questionDto.getText())
                    .build();
            question = questionRepository.save(question);
            answer = Answer.builder()
                    .answerMetaId(answerDto.getId())
                    .questionId(question.getId())
                    .questionMetaId(question.getMetaId())
                    .text(answerDto.getText())
                    .sessionId(SESSION_ID)
                    .build();
        }
        answerRepository.save(answer);
    }
}
