package zerobase.weather.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INVALID_DATE("너무 과거 혹은 먼 미래의 날짜입니다."),
    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다."),
    START_DATE_EXCEED_END_DATE("시작날짜가 종료날짜보다 큽니다."),
    DIARY_IS_EMPTY("해당 날짜에 해당하는 일기가 없습니다.");
    private final String description;
}
