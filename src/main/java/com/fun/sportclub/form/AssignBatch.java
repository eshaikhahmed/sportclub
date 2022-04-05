package com.fun.sportclub.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignBatch {
    private Long subscriptionId;
    private Long batchId;
}
