package com.hongik.service.study;

import static java.util.stream.Collectors.toList;

import com.hongik.domain.study.StudySessionRepository;
import com.hongik.domain.weekly.Weekly;
import com.hongik.domain.weekly.WeeklyRepository;
import com.hongik.dto.study.response.StudyRanking;
import com.hongik.dto.study.response.StudyRankingResponse;
import com.hongik.dto.study.response.WeeklyRankingResponse;
import com.hongik.exception.AppException;
import com.hongik.exception.ErrorCode;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StudyRankingService {

	private final StudySessionRepository studySessionRepository;

	private final WeeklyRepository weeklyRepository;

	/**
	 * 현재 주차의 랭킹과 이전 주차의 랭킹을 구합니다. 이전 주차의 랭킹을 Map에 담고 현재 주차의 랭킹차이를 구하고 결과를 조회합니다.
	 *
	 * @param yearWeek
	 * @return WeeklyRankingResponse(String, List < StudyRankingResponse >)
	 */
	public WeeklyRankingResponse getStudyDurationRanking(final int yearWeek) {

		LocalDate now = weeklyRepository.yearWeekToDate(yearWeek);

		// ex) 202440 데이터를 넣는다.
		List<StudyRanking> currentResults =
				studySessionRepository.getWeeklyStudyTimeRankingByDepartment(now, now.plusDays(7));
		// 202439 데이터를 넣는다. (이전 랭킹과 비교하기 위함)
		List<StudyRanking> previousResults =
				studySessionRepository.getWeeklyStudyTimeRankingByDepartment(now.minusDays(7), now);

		Weekly weekly = weeklyRepository.findByWeekNumber(yearWeek)
				.orElseThrow(() -> new AppException(ErrorCode.INTERNAL_SERVER_ERROR,
						ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));

		// 이전 주차에 랭킹을 department, 순위를 담는다.
		Map<String, Integer> previousResultMap = previousResults.stream()
				.collect(Collectors.toMap(StudyRanking::getDepartment, StudyRanking::getRanking));

		// 현재 currentResults의 순위와, 이전 주차 previousResultMap.get("department")의 순위를 비교하여 출력한다.
		List<StudyRankingResponse> response = currentResults.stream()
				.map(result -> {
					long hours = result.getSeconds() / 3600; // 시간을 계산
					if (hours < 1) {
						return StudyRankingResponse.of(
								result.getDepartment(),
								hours,
								0, // 랭킹을 0으로 설정
								0  // 이전 랭킹도 0으로 설정 (비교하지 않음)
						);
					}
					return StudyRankingResponse.of(
							result.getDepartment(),
							hours,
							result.getRanking(),
							previousResultMap.get(result.getDepartment()) // 이전 랭킹 값
					);
				})
				.collect(toList());

		return WeeklyRankingResponse.of(weekly.getWeekName(), response);
	}
}
