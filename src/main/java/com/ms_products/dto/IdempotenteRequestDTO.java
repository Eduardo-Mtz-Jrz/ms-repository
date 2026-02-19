package com.ms_products.dto;

import com.ms_products.enums.TypeEnum;

public record IdempotenteRequestDTO(Long idEpotrncyKey,
                                    Long productId,
                                    Double quantity,
                                    TypeEnum type
                                    ) {
}
