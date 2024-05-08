package com.ecocursos.ecocursos.models;

import com.ctc.wstx.shaded.msv_core.datatype.xsd.Comparator;
import com.ecocursos.ecocursos.models.enums.TipoMaterial;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.util.List;

@Entity
@Table(name = "material_curso")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialCurso implements Comparable<MaterialCurso>{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String titulo;
    private String link;
    private Integer ordenacao;
    @Enumerated(EnumType.ORDINAL)
    private TipoMaterial tipoMaterial;

    @ManyToOne
    @JoinColumn(name = "documento_id")
    private Documento documento;

    @Lob
    private byte[] conteudo;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @JsonIgnoreProperties({"materialCurso"})
    @OneToMany(mappedBy = "materialCurso")
    private List<AnexoMaterialCurso> anexos;

    @Column(nullable = true)
    private String vimeoID;

    @Override
    public int compareTo(MaterialCurso outroMaterial) {
        return Integer.compare(this.ordenacao, outroMaterial.ordenacao);
    }

}
