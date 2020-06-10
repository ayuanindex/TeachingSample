package com.realmax.base.signature;

import com.realmax.base.network.HttpUtil;

@HttpUtil.POST("https://ocr.tencentcloudapi.com/")
public class NumberPlateResultBean {

    /**
     * Response : {"Number":"京N0L9U8","Confidence":99,"RequestId":"210103d3-db06-4691-abe0-c0853aae606b"}
     */

    private ResponseBean Response;

    public ResponseBean getResponse() {
        return Response;
    }

    public void setResponse(ResponseBean Response) {
        this.Response = Response;
    }

    public static class ResponseBean {
        /**
         * Number : 京N0L9U8
         * Confidence : 99
         * RequestId : 210103d3-db06-4691-abe0-c0853aae606b
         */

        private String Number;
        private int Confidence;
        private String RequestId;

        public String getNumber() {
            return Number;
        }

        public void setNumber(String Number) {
            this.Number = Number;
        }

        public int getConfidence() {
            return Confidence;
        }

        public void setConfidence(int Confidence) {
            this.Confidence = Confidence;
        }

        public String getRequestId() {
            return RequestId;
        }

        public void setRequestId(String RequestId) {
            this.RequestId = RequestId;
        }

        @Override
        public String toString() {
            return "ResponseBean{" +
                    "Number='" + Number + '\'' +
                    ", Confidence=" + Confidence +
                    ", RequestId='" + RequestId + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "NumberPlateResultBean{" +
                "Response=" + Response +
                '}';
    }
}
