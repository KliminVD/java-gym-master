package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();
    private HashMap<Coach, Integer> coachesCounter = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule =
                timetable.computeIfAbsent(day, k -> new TreeMap<>());
        List<TrainingSession> sessionsAtTime =
                daySchedule.computeIfAbsent(time, k -> new ArrayList<>());

        sessionsAtTime.add(trainingSession);

        Coach coach = trainingSession.getCoach();
        coachesCounter.put(coach, coachesCounter.getOrDefault(coach, 0) + 1);
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null) {
            return new TreeMap<>();
        }

        return daySchedule;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null || daySchedule.isEmpty()) {
            return Collections.emptyList();
        }

        List<TrainingSession> sessions = daySchedule.get(timeOfDay);
        return sessions != null && !sessions.isEmpty() ? new ArrayList<>(sessions) : Collections.emptyList();
    }

    public List<CounterOfTrainings> getCountByCoaches() { // Метод для подсчета тренировок у каждого тренера
        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachesCounter.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue())); // Данные собираются в список из CounterOfTrainings
        }
        result.sort((c1, c2) -> Integer.compare(c2.getCount(), c1.getCount())); // Сортировка в обратном порядке (от большого к меньшему)

        return result;
    }
}
