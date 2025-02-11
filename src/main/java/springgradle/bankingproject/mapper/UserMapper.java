package springgradle.bankingproject.mapper;

import org.mapstruct.Mapper;
import springgradle.bankingproject.features.auth.dto.RegisterRequest;
import springgradle.bankingproject.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // domain modal(target) and source
    User fromRegisterRequest(RegisterRequest registerRequest);
}
