package com.speant.delivery.Models;

import java.util.List;

public class PayoutsResponse {
    private boolean status;

    private String pending_payout;

    private List<PayoutTransactionHistory> payout_history;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getPending_payout() {
        return pending_payout;
    }

    public void setPending_payout(String pending_payout) {
        this.pending_payout = pending_payout;
    }

    public List<PayoutTransactionHistory> getPayout_history() {
        return payout_history;
    }

    public void setPayout_history(List<PayoutTransactionHistory> payout_history) {
        this.payout_history = payout_history;
    }

    public class PayoutTransactionHistory {

        private String transaction_id;

        private String updated_at;

        private String created_at;

        private String id;

        private String delivery_boy_id;

        private String payout_amount;

        private String status;

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDelivery_boy_id() {
            return delivery_boy_id;
        }

        public void setDelivery_boy_id(String delivery_boy_id) {
            this.delivery_boy_id = delivery_boy_id;
        }

        public String getPayout_amount() {
            return payout_amount;
        }

        public void setPayout_amount(String payout_amount) {
            this.payout_amount = payout_amount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
