package com.amerika.swe.model;
// Generated 10/09/2013 09:52:41 AM by Hibernate Tools 3.2.1.GA



/**
 * SweFtecnXsolId generated by hbm2java
 */
public class SweFtecnXsolId  implements java.io.Serializable {


     private int idEstimacion;
     private int idfacttecnico;

    public SweFtecnXsolId() {
    }

    public SweFtecnXsolId(int idEstimacion, int idfacttecnico) {
       this.idEstimacion = idEstimacion;
       this.idfacttecnico = idfacttecnico;
    }
   
    public int getIdEstimacion() {
        return this.idEstimacion;
    }
    
    public void setIdEstimacion(int idEstimacion) {
        this.idEstimacion = idEstimacion;
    }
    public int getIdfacttecnico() {
        return this.idfacttecnico;
    }
    
    public void setIdfacttecnico(int idfacttecnico) {
        this.idfacttecnico = idfacttecnico;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof SweFtecnXsolId) ) return false;
		 SweFtecnXsolId castOther = ( SweFtecnXsolId ) other; 
         
		 return (this.getIdEstimacion()==castOther.getIdEstimacion())
 && (this.getIdfacttecnico()==castOther.getIdfacttecnico());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdEstimacion();
         result = 37 * result + this.getIdfacttecnico();
         return result;
   }   


}


