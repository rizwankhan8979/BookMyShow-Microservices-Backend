package movieBookingapplication.Enum;

public enum ShowSeatStatus {

    AVAILABLE, // 🟢 Khali hai (Koi bhi le sakta hai)
    LOCKED,    // 🟡 Reserved hai 10-mins ke liye (Payment Process chal raha hai)
    BOOKED     // 🔴 Permanently Book ho chuki hai

}
