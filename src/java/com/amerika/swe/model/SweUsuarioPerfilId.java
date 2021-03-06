package com.amerika.swe.model;
// Generated 26/11/2013 01:46:12 PM by Hibernate Tools 3.2.1.GA



/**
 * SweUsuarioPerfilId generated by hbm2java
 */
public class SweUsuarioPerfilId  implements java.io.Serializable {


     private String codigoUsuario;
     private String codigoPerfil;

    public SweUsuarioPerfilId() {
    }

    public SweUsuarioPerfilId(String codigoUsuario, String codigoPerfil) {
       this.codigoUsuario = codigoUsuario;
       this.codigoPerfil = codigoPerfil;
    }
   
    public String getCodigoUsuario() {
        return this.codigoUsuario;
    }
    
    public void setCodigoUsuario(String codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
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
		 if ( !(other instanceof SweUsuarioPerfilId) ) return false;
		 SweUsuarioPerfilId castOther = ( SweUsuarioPerfilId ) other; 
         
		 return ( (this.getCodigoUsuario()==castOther.getCodigoUsuario()) || ( this.getCodigoUsuario()!=null && castOther.getCodigoUsuario()!=null && this.getCodigoUsuario().equals(castOther.getCodigoUsuario()) ) )
 && ( (this.getCodigoPerfil()==castOther.getCodigoPerfil()) || ( this.getCodigoPerfil()!=null && castOther.getCodigoPerfil()!=null && this.getCodigoPerfil().equals(castOther.getCodigoPerfil()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getCodigoUsuario() == null ? 0 : this.getCodigoUsuario().hashCode() );
         result = 37 * result + ( getCodigoPerfil() == null ? 0 : this.getCodigoPerfil().hashCode() );
         return result;
   }   


}


