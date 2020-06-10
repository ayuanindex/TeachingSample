package com.realmax.base.signature;

import com.realmax.base.network.HttpUtil;

@HttpUtil.POST("https://ocr.tencentcloudapi.com/")
public class NumberPlateResultBean {

    /**
     * Response : {"Number":"京N0L9U8","Confidence":99,"Error":{"Code":"FailedOperation.OcrFailed","Message":"Ocr识别失败"},"RequestId":"210103d3-db06-4691-abe0-c0853aae606b"}
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
         * Error : {"Code":"FailedOperation.OcrFailed","Message":"Ocr识别失败"}
         * RequestId : 210103d3-db06-4691-abe0-c0853aae606b
         */

        private String Number;
        private int Confidence;
        private ErrorBean Error;
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

        public ErrorBean getError() {
            return Error;
        }

        public void setError(ErrorBean Error) {
            this.Error = Error;
        }

        public String getRequestId() {
            return RequestId;
        }

        public void setRequestId(String RequestId) {
            this.RequestId = RequestId;
        }

        public static class ErrorBean {
            /**
             * Code : FailedOperation.OcrFailed
             * Message : Ocr识别失败
             */

            private String Code;
            private String Message;

            public String getCode() {
                return Code;
            }

            public void setCode(String Code) {
                this.Code = Code;
            }

            public String getMessage() {
                return Message;
            }

            public void setMessage(String Message) {
                this.Message = Message;
            }

            @Override
            public String toString() {
                return "ErrorBean{" +
                        "Code='" + Code + '\'' +
                        ", Message='" + Message + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "ResponseBean{" +
                    "Number='" + Number + '\'' +
                    ", Confidence=" + Confidence +
                    ", Error=" + Error +
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
