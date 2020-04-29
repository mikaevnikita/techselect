package ru.stankin.mikaev.techselect.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stankin.mikaev.techselect.dto.AnswerDto;
import ru.stankin.mikaev.techselect.dto.QuestionDto;
import ru.stankin.mikaev.techselect.dto.SurveyDto;
import ru.stankin.mikaev.techselect.exception.QuestionDoesNotContainsAnswerException;
import ru.stankin.mikaev.techselect.exception.SurveyNotCompletedException;
import ru.stankin.mikaev.techselect.model.Answer;
import ru.stankin.mikaev.techselect.model.AnswerMeta;
import ru.stankin.mikaev.techselect.model.Question;
import ru.stankin.mikaev.techselect.model.QuestionMeta;
import ru.stankin.mikaev.techselect.repository.AnswerMetaRepository;
import ru.stankin.mikaev.techselect.repository.AnswerRepository;
import ru.stankin.mikaev.techselect.repository.QuestionMetaRepository;
import ru.stankin.mikaev.techselect.repository.QuestionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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

    public Optional<QuestionDto> getNextQuestion(QuestionDto questionDto, UUID sessionId) {
        if (questionDto == null) {
            Optional<Question> lastQuestion = questionRepository.findLastQuestion(sessionId);
            if (lastQuestion.isPresent()) {
                Long lastQuestionMetaId = lastQuestion.get().getMetaId();
                Optional<QuestionMeta> nextQuestionMetaOpt = questionMetaRepository.findById(lastQuestionMetaId + 1);
                if (nextQuestionMetaOpt.isPresent()) {
                    QuestionMeta nextQuestionMeta = nextQuestionMetaOpt.get();
                    List<AnswerMeta> questionAnswers =
                            answerMetaRepository.findAllByQuestionMetaId(nextQuestionMeta.getId());
                    Answer selectedAnswer = answerRepository.findBySessionIdAndQuestionMetaId(sessionId,
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
                Answer selectedAnswer = answerRepository.findBySessionIdAndQuestionMetaId(sessionId,
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

    public Optional<QuestionDto> getPrevQuestion(QuestionDto currentQuestion, UUID sessionId) {
        Optional<Question> lastQuestion = questionRepository.findBySessionIdAndMetaId(sessionId,
                currentQuestion.getId() - 1);
        if (lastQuestion.isPresent()) {
            Question question = lastQuestion.get();
            QuestionMeta questionMeta = questionMetaRepository.findById(question.getMetaId()).get();
            List<AnswerMeta> questionAnswers = answerMetaRepository.findAllByQuestionMetaId(questionMeta.getId());
            Answer selectedAnswer = answerRepository.findBySessionIdAndQuestionMetaId(sessionId, questionMeta.getId());
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
    public void sumbitAnswer(QuestionDto questionDto, AnswerDto answerDto, UUID sessionId) {
        checkIsAssignable(questionDto, answerDto);
        Answer answer = answerRepository.findBySessionIdAndQuestionMetaId(sessionId, questionDto.getId());
        if (answer != null) {
            answer.setAnswerMetaId(answerDto.getId());
            answer.setText(answerDto.getText());
        } else {
            Question question = Question.builder()
                    .metaId(questionDto.getId())
                    .sessionId(sessionId)
                    .text(questionDto.getText())
                    .build();
            question = questionRepository.save(question);
            answer = Answer.builder()
                    .answerMetaId(answerDto.getId())
                    .questionId(question.getId())
                    .questionMetaId(question.getMetaId())
                    .text(answerDto.getText())
                    .sessionId(sessionId)
                    .build();
        }
        answerRepository.save(answer);
    }

    public SurveyDto getCompletedSurvey(UUID sessionId) {
        Long questionsCount = questionMetaRepository.getQuestionsCount();
        Long questionsCountByUser = questionRepository.getQuestionsCountByUser(sessionId);
        if (questionsCountByUser != questionsCount) {
            throw new SurveyNotCompletedException();
        }
        List<QuestionDto> questionDtos = new ArrayList<>();
        List<Question> allBySessionId = questionRepository.findAllBySessionId(sessionId);
        for (Question question : allBySessionId) {
            QuestionMeta questionMeta = questionMetaRepository.findById(question.getMetaId()).get();
            List<AnswerMeta> questionAnswers =
                    answerMetaRepository.findAllByQuestionMetaId(questionMeta.getId());
            Answer selectedAnswer = answerRepository.findBySessionIdAndQuestionMetaId(sessionId, questionMeta.getId());
            QuestionDto resultQuestionDto = buildQuestionWithAnswers(questionMeta, questionAnswers);
            if (selectedAnswer != null) {
                resultQuestionDto.setSelectedAnswer(selectedAnswer.getAnswerMetaId());
            }
            questionDtos.add(resultQuestionDto);
        }
        return new SurveyDto(questionDtos);
    }
}
