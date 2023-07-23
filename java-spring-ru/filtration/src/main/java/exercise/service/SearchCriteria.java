package exercise.service;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteria<T> {
    private String key;
    private T value;

    public SearchCriteria(String key, T value) {
        this.key = key;
        this.value = value;
    }
}
