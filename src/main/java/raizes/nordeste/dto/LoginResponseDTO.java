package raizes.nordeste.dto;

import raizes.nordeste.model.TipoUsuario;

public record LoginResponseDTO(
        String accessToken,
        String tokenType,
        long expiresIn,
        TipoUsuario tipoUsuario
) {
}
