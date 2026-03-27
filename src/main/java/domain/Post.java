package domain;

import java.time.LocalDateTime;
public record Post(int id, int userId,String text,LocalDateTime ts) {
}
