package dmit2015.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.datafaker.Faker;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemDto {

    private Long id;

    private String name;

    private boolean complete;

    private Integer version;

    public static TodoItemDto of(Faker faker) {
        TodoItemDto dto = new TodoItemDto();
        dto.setComplete(false);
        dto.setName("Watch " + faker.breakingBad().episode());
        return dto;
    }


}