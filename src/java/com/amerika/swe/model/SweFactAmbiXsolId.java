package com.amerika.swe.model;
// Generated 10/09/2013 09:52:41 AM by Hibernate Tools 3.2.1.GA



/**
 * SweFactAmbiXsolId generated by hbm2java
 */
public class SweFactAmbiXsolId  implements java.io.Serializable {


     private int idfactambietal;
     private int idEstimacion;

    public SweFactAmbiXsolId() {
    }

    public SweFactAmbiXsolId(int idfactambietal, int idEstimacion) {
       this.idfactambietal = idfactambietal;
       this.idEstimacion = idEstimacion;
    }
   
    public int getIdfactambietal() {
        return this.idfactambietal;
    }
    
    public void setIdfactambietal(int idfactambietal) {
        this.idfactambietal = idfactambietal;
    }
    public int getIdEstimacion() {
        return this.idEstimacion;
    }
    
    public void setIdEstimacion(int idEstimacion) {
        this.idEstimacion = idEstimacion;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof SweFactAmbiXsolId) ) return false;
		 SweFactAmbiXsolId castOther = ( SweFactAmbiXsolId ) other; 
         
		 return (this.getIdfactambietal()==castOther.getIdfactambietal())
 && (this.getIdEstimacion()==castOther.getIdEstimacion());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdfactambietal();
         result = 37 * result + this.getIdEstimacion();
         return result;
   }   


}


