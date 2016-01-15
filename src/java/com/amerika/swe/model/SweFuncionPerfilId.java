package com.amerika.swe.model;
// Generated 26/11/2013 01:46:12 PM by Hibernate Tools 3.2.1.GA



/**
 * SweFuncionPerfilId generated by hbm2java
 */
public class SweFuncionPerfilId  implements java.io.Serializable {


     private int codigoFuncion;
     private String codigoPerfil;

    public SweFuncionPerfilId() {
    }

    public SweFuncionPerfilId(int codigoFuncion, String codigoPerfil) {
       this.codigoFuncion = codigoFuncion;
       this.codigoPerfil = codigoPerfil;
    }
   
    public int getCodigoFuncion() {
        return this.codigoFuncion;
    }
    
    public void setCodigoFuncion(int codigoFuncion) {
        this.codigoFuncion = codigoFuncion;
    }
    public String getCodigoPerfil() {
        return this.codigoPerfil;
    }
    
    public void setCodigoPerfil(String codigoPerfil) {
        this.codigoPerfil = codigoPerfil;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof SweFuncionPerfilId) ) return false;
		 SweFuncionPerfilId castOther = ( SweFuncionPerfilId ) other; 
         
		 return (this.getCodigoFuncion()==castOther.getCodigoFuncion())
 && ( (this.getCodigoPerfil()==castOther.getCodigoPerfil()) || ( this.getCodigoPerfil()!=null && castOther.getCodigoPerfil()!=null && this.getCodigoPerfil().equals(castOther.getCodigoPerfil()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getCodigoFuncion();
         result = 37 * result + ( getCodigoPerfil() == null ? 0 : this.getCodigoPerfil().hashCode() );
         return result;
   }   


}

