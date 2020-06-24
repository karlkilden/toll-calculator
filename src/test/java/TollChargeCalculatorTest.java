import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TollChargeCalculatorTest {
    @Test
    void tollChargeCalc_can_calculate_no_charge() {
        TollChargeCalculator tc = new TollChargeCalculator();
        Assertions.assertThat(tc.calcCharge(VehicleType.CAR)).isOne();
    }
}