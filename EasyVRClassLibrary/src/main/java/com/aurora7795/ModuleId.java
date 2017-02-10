package com.aurora7795;

/**
 * Module identification number (firmware version)
 */

public enum ModuleId {
    VRBOT(0), //*< Identifies a VRbot module
    EASYVR(1), //*< Identifies an EasyVR module
    EASYVR2(2), //*< Identifies an EasyVR module version 2
    EASYVR2_3(3), //*< Identifies an EasyVR module version 2, firmware revision 3
    EASYVR3(8), //*< Identifies an EasyVR module version 3, firmware revision 0
    EASYVR3_1(9), //*< Identifies an EasyVR module version 3, firmware revision 1
    EASYVR3_2(10), //*< Identifies an EasyVR module version 3, firmware revision 2
    EASYVR3_3(11), //*< Identifies an EasyVR module version 3, firmware revision 3
    EASYVR3_4(12),; //*< Identifies an EasyVR module version 3, firmware revision 4

    private int value;

    ModuleId(int i) {
        this.value = i;
    }

    public int getValue() {
        return value;
    }
}
