package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

public class TimetableTest {

    @Test
    void testAddSingleTrainingSession() { // Тестирование добавления одной тренировки
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession session = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(session);

        Map<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(session, mondaySessions.get(new TimeOfDay(13, 0)).getFirst());
    }

    @Test
    void testAddMultipleSessionsOnSameDay() { // Добавление нескольких тренировок в один день, но разное время
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        TrainingSession session1 = new TrainingSession(group, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession session2 = new TrainingSession(group, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(session2); // для наглядности тренировки добавляются в обратном по времени порядке
        timetable.addNewTrainingSession(session1);

        Map<TimeOfDay, List<TrainingSession>> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);
        Assertions.assertEquals(2, thursdaySessions.size());
        List<TimeOfDay> times = new ArrayList<>(thursdaySessions.keySet());
        Assertions.assertEquals(new TimeOfDay(13, 0), times.get(0));
        Assertions.assertEquals(new TimeOfDay(20, 0), times.get(1));

        Assertions.assertEquals(session1, thursdaySessions.get(new TimeOfDay(13, 0)).getFirst()); // сначала тренировка на 13:00
        Assertions.assertEquals(session2, thursdaySessions.get(new TimeOfDay(20, 0)).getFirst()); // потом на 20:00
    }

    @Test
    void testAddMultipleSessionsAtSameTime() { // Добавление нескольких разных тренировок с разными тренерами и группами в одно время
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Измайлов", "Кирилл", "Викторович");
        Coach coach2 = new Coach("Семёнов", "Владимир", "Константинович");

        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Гимнастика для детей", Age.CHILD, 45);

        TrainingSession session1 = new TrainingSession(group1, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));

        Assertions.assertEquals(2, sessions.size());
        Assertions.assertTrue(sessions.contains(session1)); // Обе группы должны заниматься в одно время
        Assertions.assertTrue(sessions.contains(session2));
    }

    @Test
    void testAddSessionsOnDifferentDays() { // Добавление тренировок в разные дни
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);

        TrainingSession monday = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession friday = new TrainingSession(group, coach,
                DayOfWeek.FRIDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(monday);
        timetable.addNewTrainingSession(friday);

        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).size()); // Тренировки хранятся в отдельных бакетах по дням
        Assertions.assertEquals(1, timetable.getTrainingSessionsForDay(DayOfWeek.FRIDAY).size());
        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.WEDNESDAY).isEmpty()); // Тренировок за другой день быть не должно
    }

    @Test
    void testGetTrainingSessionsForDaySingleSession() { // Получение данных о тренировке по дням
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY); // В понедельник одна тренировка
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(singleTrainingSession, mondaySessions.get(new TimeOfDay(13, 0)).getFirst());
        //Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY); // В четверг ничего нет
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() { // Получение данных о нескольких тренировках
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        Map<TimeOfDay, List<TrainingSession>> mondaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY); // В понедельник будет одно занятие
        Assertions.assertEquals(1, mondaySessions.size());
        Assertions.assertEquals(mondayChildTrainingSession, mondaySessions.get(new TimeOfDay(13, 0)).getFirst());
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        Map<TimeOfDay, List<TrainingSession>> thursdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY); // В четверг два занятия в отсортированном по времени порядке
        Assertions.assertEquals(2, thursdaySessions.size());
        List<TimeOfDay> times = new ArrayList<>(thursdaySessions.keySet());
        Assertions.assertEquals(new TimeOfDay(13, 0), times.get(0));
        Assertions.assertEquals(new TimeOfDay(20, 0), times.get(1));
        Assertions.assertEquals(thursdayChildTrainingSession, thursdaySessions.get(new TimeOfDay(13, 0)).getFirst());
        Assertions.assertEquals(thursdayAdultTrainingSession, thursdaySessions.get(new TimeOfDay(20, 0)).getFirst());
        // Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> tuesdaySessions = timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY); // Во вторник занятий нет
        Assertions.assertTrue(tuesdaySessions.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() { // Получить тренировки по определенному дню и времени
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        List<TrainingSession> sessions13 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(13, 0)); // В понедельник одно заниятие в 13
        Assertions.assertEquals(1, sessions13.size());
        Assertions.assertEquals(singleTrainingSession, sessions13.getFirst());
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> sessions14 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0)); // А на 14 нет
        Assertions.assertTrue(sessions14.isEmpty());
    }

    @Test
    void testEmptyTimetable() { // Получение тренировки из пустого расписания
        Timetable timetable = new Timetable();

        Assertions.assertTrue(timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).isEmpty()); // Поиск по дням и по времени должен вернуть пустые списки
        Assertions.assertTrue(timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY, new TimeOfDay(10, 0)).isEmpty());
    }

    @Test
    void testMultipleSessionsAtSameTime() { // Получение нескольких тренировок в один день и одно время
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Измайлов", "Кирилл", "Викторович");
        Coach coach2 = new Coach("Семёнов", "Владимир", "Константинович");

        Group group1 = new Group("Акробатика для детей", Age.CHILD, 60);
        Group group2 = new Group("Гимнастика для детей", Age.CHILD, 45);

        TrainingSession session1 = new TrainingSession(group1, coach1,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));
        TrainingSession session2 = new TrainingSession(group2, coach2,
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(session1);
        timetable.addNewTrainingSession(session2);

        List<TrainingSession> sessions = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.WEDNESDAY, new TimeOfDay(18, 0));

        Assertions.assertEquals(2, sessions.size()); // В среду в 18 должно быть две тренировки
        Assertions.assertTrue(sessions.contains(session1));
        Assertions.assertTrue(sessions.contains(session2));
    }

    @Test
    void testGetSessionsForTimeWhenDayHasOtherSessions() { // Получение тренировки в конкретное время, когда в расписании в этот день есть и другие
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        // В один день добавляются несколько тренировок
        TrainingSession session13 = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession session15 = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(15, 0));
        TrainingSession session18 = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(18, 0));

        timetable.addNewTrainingSession(session13);
        timetable.addNewTrainingSession(session15);
        timetable.addNewTrainingSession(session18);

        List<TrainingSession> sessions15 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(15, 0));  // В 15 только одна тренировка

        Assertions.assertEquals(1, sessions15.size());
        Assertions.assertEquals(session15, sessions15.getFirst());

        List<TrainingSession> sessions14 = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(14, 0));  // В 14 нет тренировок
        Assertions.assertTrue(sessions14.isEmpty());
    }

    @Test
    void testGetCountByCoachesSingleCoach() { // Подсчет нескольких тренировок у одного тренера
        Timetable timetable = new Timetable();
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Прыжки на батутах для детей", Age.CHILD, 60);
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach, DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));
        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        Assertions.assertEquals(1, result.size()); // Всего один тренер
        Assertions.assertEquals(coach, result.getFirst().getCoach());
        Assertions.assertEquals(3, result.getFirst().getCount()); // У него три тренеровки
    }

    @Test
    void testGetCountByCoachesMultipleCoachesSortedDescending() { // Несколько тренеров с разным количеством тренеровок
        Timetable timetable = new Timetable();

        Coach coach1 = new Coach("Измайлов", "Кирилл", "Викторович");
        Coach coach2 = new Coach("Семёнов", "Владимир", "Константинович");
        Coach coach3 = new Coach("Васильев", "Николай", "Сергеевич");
        Group group = new Group("Прыжки на батутах для детей", Age.CHILD, 60);
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.WEDNESDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach1, DayOfWeek.FRIDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach2, DayOfWeek.TUESDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.THURSDAY, new TimeOfDay(15, 0)));
        timetable.addNewTrainingSession(new TrainingSession(group, coach3, DayOfWeek.SATURDAY, new TimeOfDay(15, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        Assertions.assertEquals(3, result.size()); // 3 тренера в порядке убывания количества тренировок
        Assertions.assertEquals(coach1, result.get(0).getCoach());
        Assertions.assertEquals(3, result.get(0).getCount()); // у первого тренера 3 тренировки
        Assertions.assertEquals(coach3, result.get(1).getCoach());
        Assertions.assertEquals(2, result.get(1).getCount()); // у третьего 2
        Assertions.assertEquals(coach2, result.get(2).getCoach());
        Assertions.assertEquals(1, result.get(2).getCount()); // у второго 1
    }

    @Test
    void testGetCountByCoachesEmptyTimetable() { // Подсчет тренеровок в пустом расписании
        Timetable timetable = new Timetable();
        List<CounterOfTrainings> result = timetable.getCountByCoaches();
        Assertions.assertTrue(result.isEmpty()); // Возвращается пустой список
    }
}
