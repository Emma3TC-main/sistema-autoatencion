/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.grupo1.vista;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

/**
 *
 * @author user
 */
public class Mesas extends javax.swing.JPanel {
private Principal principal;

    /**
     * Creates new form Mesas
     */
    public Mesas() {
        initComponents();
    }

    public Mesas(Principal principal) {
        this.principal = principal;
        initComponents();
                jToggleButton2.setSelected(true); // Por defecto no presionado
        jToggleButton2.setBackground(Color.RED); // Color inicial
        
        jToggleButton3.setSelected(false); // Por defecto no presionado
        jToggleButton3.setBackground(Color.GREEN); // Color inicial
        
        jToggleButton4.setSelected(false); // Por defecto no presionado
        jToggleButton4.setBackground(Color.GREEN); // Color inicial

        jToggleButton5.setSelected(true); // Por defecto no presionado
        jToggleButton5.setBackground(Color.RED); // Color inicial

        jToggleButton8.setSelected(false); // Por defecto no presionado
        jToggleButton8.setBackground(Color.GREEN); // Color inicial

        jToggleButton9.setSelected(false); // Por defecto no presionado
        jToggleButton9.setBackground(Color.GREEN); // Color inicial    

        jToggleButton6.setSelected(true); // Por defecto no presionado
        jToggleButton6.setBackground(Color.RED); // Color inicial         

        jToggleButton7.setSelected(true); // Por defecto no presionado
        jToggleButton7.setBackground(Color.RED); // Color inicial
        
        jToggleButton1.setSelected(false); // Por defecto no presionado
        jToggleButton1.setBackground(Color.GREEN); // Color inicial        

        jToggleButton10.setSelected(true); // Por defecto no presionado
        jToggleButton10.setBackground(Color.RED); // Color inicial  
    
        jToggleButton2.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (!jToggleButton2.isSelected()) {
                    // Si el usuario intenta desmarcarlo, lo volvemos a marcar
                    SwingUtilities.invokeLater(() -> {
                        jToggleButton2.setSelected(true); // Lo forzamos a mantenerse marcado
                        jToggleButton2.setBackground(Color.RED);
                    });
                }
            }
        });        
    
        jToggleButton5.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (!jToggleButton5.isSelected()) {
                    // Si el usuario intenta desmarcarlo, lo volvemos a marcar
                    SwingUtilities.invokeLater(() -> {
                        jToggleButton5.setSelected(true); // Lo forzamos a mantenerse marcado
                        jToggleButton5.setBackground(Color.RED);
                    });
                }
            }
        });  

        jToggleButton6.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (!jToggleButton6.isSelected()) {
                    // Si el usuario intenta desmarcarlo, lo volvemos a marcar
                    SwingUtilities.invokeLater(() -> {
                        jToggleButton6.setSelected(true); // Lo forzamos a mantenerse marcado
                        jToggleButton6.setBackground(Color.RED);
                    });
                }
            }
        });  

        jToggleButton7.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (!jToggleButton7.isSelected()) {
                    // Si el usuario intenta desmarcarlo, lo volvemos a marcar
                    SwingUtilities.invokeLater(() -> {
                        jToggleButton7.setSelected(true); // Lo forzamos a mantenerse marcado
                        jToggleButton7.setBackground(Color.RED);
                    });
                }
            }
        }); 
        
        jToggleButton10.addItemListener(new java.awt.event.ItemListener() {
            @Override
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (!jToggleButton10.isSelected()) {
                    // Si el usuario intenta desmarcarlo, lo volvemos a marcar
                    SwingUtilities.invokeLater(() -> {
                        jToggleButton10.setSelected(true); // Lo forzamos a mantenerse marcado
                        jToggleButton10.setBackground(Color.RED);
                    });
                }
            }
        }); 
    }

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jToggleButton2 = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();
        jToggleButton3 = new javax.swing.JToggleButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jToggleButton4 = new javax.swing.JToggleButton();
        jLabel5 = new javax.swing.JLabel();
        jToggleButton5 = new javax.swing.JToggleButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jToggleButton6 = new javax.swing.JToggleButton();
        jLabel8 = new javax.swing.JLabel();
        jToggleButton7 = new javax.swing.JToggleButton();
        jToggleButton8 = new javax.swing.JToggleButton();
        jLabel9 = new javax.swing.JLabel();
        jToggleButton9 = new javax.swing.JToggleButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel10 = new javax.swing.JLabel();
        jToggleButton10 = new javax.swing.JToggleButton();
        jLabel11 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Mesas Disponibles");

        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/4_sillas (1).png"))); // NOI18N
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Mesa N°1");

        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/4_sillas (1).png"))); // NOI18N
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Mesa N°2");

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Mesa N°4");

        jToggleButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/4_sillas (1).png"))); // NOI18N
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Mesa N°3");

        jToggleButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/4_sillas (1).png"))); // NOI18N
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Mesa N°6");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Mesa N°8");

        jToggleButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/4_sillas (1).png"))); // NOI18N
        jToggleButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton6ActionPerformed(evt);
            }
        });

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Mesa N°7");

        jToggleButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/4_sillas (1).png"))); // NOI18N
        jToggleButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton7ActionPerformed(evt);
            }
        });

        jToggleButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/4_sillas (1).png"))); // NOI18N
        jToggleButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton8ActionPerformed(evt);
            }
        });

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Mesa N°5");

        jToggleButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/4_sillas (1).png"))); // NOI18N
        jToggleButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton9ActionPerformed(evt);
            }
        });

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/8_sillas.png"))); // NOI18N
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Mesa N°9");

        jToggleButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/8_sillas.png"))); // NOI18N
        jToggleButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton10ActionPerformed(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Mesa N°10");

        jButton1.setText("Regresar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Continuar");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(60, 60, 60)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(60, 60, 60)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(60, 60, 60)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jToggleButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jToggleButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(60, 60, 60)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jToggleButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(60, 60, 60)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jToggleButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(60, 60, 60)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jToggleButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jToggleButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)))
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)))
                .addGap(69, 69, 69)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel10))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jToggleButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        principal.mostrarPanel("ParaLlevar");

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton2.isSelected()) {
            jToggleButton2.setBackground(Color.RED);
        } else {
            jToggleButton2.setBackground(Color.GREEN);
    
        }
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton3.isSelected()) {
            jToggleButton3.setBackground(Color.RED);
        } else {
            jToggleButton3.setBackground(Color.GREEN);
    
        }
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton4.isSelected()) {
            jToggleButton4.setBackground(Color.RED);
        } else {
            jToggleButton4.setBackground(Color.GREEN);
    
        }
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton5.isSelected()) {
            jToggleButton5.setBackground(Color.RED);
        } else {
            jToggleButton5.setBackground(Color.GREEN);
    
        }
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jToggleButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton8ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton8.isSelected()) {
            jToggleButton8.setBackground(Color.RED);
        } else {
            jToggleButton8.setBackground(Color.GREEN);
    
        }
    }//GEN-LAST:event_jToggleButton8ActionPerformed

    private void jToggleButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton9ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton9.isSelected()) {
            jToggleButton9.setBackground(Color.RED);
        } else {
            jToggleButton9.setBackground(Color.GREEN);
    
        }
    }//GEN-LAST:event_jToggleButton9ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton6.isSelected()) {
            jToggleButton6.setBackground(Color.RED);
        } else {
            jToggleButton6.setBackground(Color.GREEN);
    
        }
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    private void jToggleButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton7ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton7.isSelected()) {
            jToggleButton7.setBackground(Color.RED);
        } else {
            jToggleButton7.setBackground(Color.GREEN);
    
        }
    }//GEN-LAST:event_jToggleButton7ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton1.isSelected()) {
            jToggleButton1.setBackground(Color.RED);
        } else {
            jToggleButton1.setBackground(Color.GREEN);
    
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton10ActionPerformed
        // TODO add your handling code here:
        if (jToggleButton10.isSelected()) {
            jToggleButton10.setBackground(Color.RED);
        } else {
            jToggleButton10.setBackground(Color.GREEN);
    
        }
    }//GEN-LAST:event_jToggleButton10ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton10;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JToggleButton jToggleButton7;
    private javax.swing.JToggleButton jToggleButton8;
    private javax.swing.JToggleButton jToggleButton9;
    // End of variables declaration//GEN-END:variables
}
